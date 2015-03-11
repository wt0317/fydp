<?php
	require_once "RestServer/RestServer.php";

        header('Content-type: application/json');
  
	class Service {
		public function itemIn($ShelfId, $ShelfPassword, $ShelfRegions, $Amount){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$shelfAuth = $DB_Connect->shelfAuth($ShelfId, $ShelfPassword);
			
			if($shelfAuth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$ShelfRegions = explode(",", $ShelfRegions);
			
			$getUserForShelf = mysql_query("SELECT username FROM shelfUserMapping WHERE ShelfId='".$ShelfId."' LIMIT 1");
			
			if(mysql_num_rows($getUserForShelf) != 0){
				while($getUser = mysql_fetch_array($getUserForShelf)){
					$username = $getUser[0];
				}
				
				$checkForNewItems = mysql_query("SELECT * FROM pendingInventory WHERE username='".$username."' ORDER BY Status DESC LIMIT 1");
				
				//New Item Added
				if(mysql_num_rows($checkForNewItems) != 0){
					while($getData = mysql_fetch_array($checkForNewItems)){
						$Barcode = $getData['Barcode'];
						$CategoryId = $getData['CategoryId'];
						$DateAdded = $getData['DateAdded'];
						$ExpiryDate = $getData['ExpiryDate'];
						$Name = $getData['Name'];
						$Price = $getData['Price'];
						$Status = $getData['Status'];
					}
					
					for($i = 0; $i < sizeof($ShelfRegions); $i++){
						mysql_query("INSERT INTO inventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price, InitialAmount, CurrentAmount, Status, ShelfID, ShelfRegion) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".$DateAdded."', '".$ExpiryDate."', '".$Name."', '".$Price."', '".$Amount."', '".$Amount."', '0', '".$ShelfId."', '".$ShelfRegions[$i]."')");
					}
					
					mysql_query("DELETE FROM pendingInventory WHERE username='".$username."' AND Status='".$Status."'");
				}
				//Item Checked Back In
				else{
					$getDataQuery = mysql_query("SELECT * FROM inventory WHERE username='".$username."' AND Status='1'");
					
					if(mysql_num_rows($getDataQuery) == 0){
						$DB_Connect->close();
						return array("Error" => "No items are currently checked out.");
					}
					while($getData = mysql_fetch_array($getDataQuery)){
						$Barcode = $getData['Barcode'];
						$CategoryId = $getData['CategoryId'];
						$DateAdded = $getData['DateAdded'];
						$ExpiryDate = $getData['ExpiryDate'];
						$Name = $getData['Name'];
						$Price = $getData['Price'];
						$InitialAmount = $getData['InitialAmount'];
					}
					
					mysql_query("DELETE FROM inventory WHERE username='".$username."' AND Status='1'");
					mysql_query("UPDATE inventory SET Status=Status-1 WHERE username='".$username."' AND Status<>'0'");
					
					for($i = 0; $i < sizeof($ShelfRegions); $i++){
						mysql_query("INSERT INTO inventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price, InitialAmount, CurrentAmount, Status, ShelfID, ShelfRegion) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".$DateAdded."', '".$ExpiryDate."', '".$Name."', '".$Price."', '".$InitialAmount."', '".$Amount."', '0', '".$ShelfId."', '".$ShelfRegions[$i]."')");
					}
				}
			}
			else{
				$DB_Connect->close();
				return array("Error" => "Shelf not associated with any users.");
			}

			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function itemOut($ShelfId, $ShelfPassword, $ShelfRegions){
			$curOut = 1;
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$shelfAuth = $DB_Connect->shelfAuth($ShelfId, $ShelfPassword);
			
			if($shelfAuth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$getUserForShelf = mysql_query("SELECT username FROM shelfUserMapping WHERE ShelfId='".$ShelfId."' LIMIT 1");
			
			if(mysql_num_rows($getUserForShelf) != 0){
				while($getUser = mysql_fetch_array($getUserForShelf)){
					$username = $getUser[0];
				}
			
				$ShelfRegions = explode(",", $ShelfRegions);
				$ShelfRegionCOr = "(ShelfRegion='".$ShelfRegions[0]."'";
				for($i = 1; $i < sizeof($ShelfRegions); $i++){
					$ShelfRegionCOr .= " OR ShelfRegion='".$ShelfRegions[$i]."'";
				}
				$ShelfRegionCOr .= ")";
				
				$checkShelfRegion = mysql_query("SELECT MAX(Status) FROM inventory WHERE username='".$username."' AND Status<>'0' LIMIT 1");
				
				if(mysql_num_rows($checkShelfRegion) > 0){
					while($getCurOut = mysql_fetch_array($checkShelfRegion)){
						$curOut = $getCurOut[0] + 1;
					}
				}
				
				$checkShelfRegion = mysql_query("SELECT Status FROM inventory WHERE username='".$username."' AND ShelfId='".$ShelfId."' AND ".$ShelfRegionCOr);
			
				if(mysql_num_rows($checkShelfRegion) == 0){
					$DB_Connect->close();
					return array("Error" => "Invalid Shelf ID and Region combination");
				}
				
				mysql_query("UPDATE inventory SET Status='".$curOut."', ShelfId='-1', ShelfRegion='-1' WHERE username='".$username."' AND ShelfId='".$ShelfId."' AND ".$ShelfRegionCOr);
			}
			else{
				$DB_Connect->close();
				return array("Error" => "Shelf not associated with any users.");
			}
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function addItem($username, $password, $Barcode, $CategoryId, $ExpiryDate, $Name, $Price){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			$Format = new Format();
			$ret = array();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$getMaxStatus = mysql_query("SELECT MAX(Status) FROM pendingInventory WHERE username='".$username."' LIMIT 1");
			
			if(mysql_num_rows($getMaxStatus) == 0){
				mysql_query("INSERT INTO pendingInventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".time()."', '".$ExpiryDate."', '".$Name."', '".$Price."')");
			}
			else{
				while($getStatus = mysql_fetch_array($getMaxStatus)){
					$Status = $getStatus[0] + 1;
				}
				
				mysql_query("INSERT INTO pendingInventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price, Status) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".time()."', '".$ExpiryDate."', '".$Name."', '".$Price."', '".$Status."')");
			}

			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function removeItem($username, $password, $Status){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			if($Status == 0){
				$DB_Connect->close();
				return array("Success" => "false", "Error" => "Item must be checked out first.");
			}
			
			mysql_query("DELETE FROM inventory WHERE username='".$username."' AND Status='".$Status."'");
			
			mysql_query("UPDATE inventory SET Status=Status-1 WHERE username='".$username."' AND Status>'".$Status."'");
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function getInventory($username, $password){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			$Format = new Format();
			$ret = array();
			$iArray = array();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$getShelfIds = mysql_query("SELECT Distinct(ShelfId) FROM inventory WHERE username='".$username."'");
			
			if(mysql_num_rows($getShelfIds) == 0){
				$DB_Connect->close();
				return array("Inventory" => "Empty");
			}

			while($ShelfIds = mysql_fetch_array($getShelfIds)) {
				$ShelfId = $ShelfIds['ShelfId'];
				
				$getInventory = mysql_query("SELECT * FROM inventory WHERE username='".$username."' AND ShelfId='".$ShelfId."'");
				
				while($Inventory = mysql_fetch_array($getInventory)){
					$iArray = $Format->addInventoryArray($iArray, $Inventory['InitialAmount'], $Inventory['CurrentAmount'], $Inventory['Barcode'], $Inventory['CategoryId'], $Inventory['DateAdded'], $Inventory['ExpiryDate'], $Inventory['Name'], $Inventory['Price'], $Inventory['ShelfRegion'], $Inventory['Status']);
				}
				
				$ret = $Format->addShelfArray($ret, $ShelfId, $iArray);
				
				$iArray = array();
			}

			$DB_Connect->close();
			return $ret;
		}
		
		public function newUser($username, $email, $password){	
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$checkUser = mysql_query("SELECT username FROM user WHERE username='".$username."'");
			
			if(mysql_num_rows($checkUser) != 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			$checkEmail = mysql_query("SELECT email FROM user WHERE email='".$email."'");
			
			if(mysql_num_rows($checkEmail) != 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			$Keygen = new Keygen();
			$secret = $Keygen->generateRandomSecret();
			$subject = "Shelf-e Registration";
			$message = "Your secret code to complete registration is: ".$secret;
			$headers = 'From: Shelf-e';
			if(filter_var($email, FILTER_VALIDATE_EMAIL)){
				mail($email, $subject, $message, $headers);
				mysql_query("INSERT INTO user (username, password, email, status) VALUES ('".$username."', '".$password."', '".$email."', '".$secret."')");
			}
			else{
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function confirmNewUser($email, $secret){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$checkUser = mysql_query("SELECT username FROM user WHERE email='".$email."' AND status='".$secret."'");
			
			if(mysql_num_rows($checkUser) == 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			else{
				$users = mysql_fetch_array($checkUser);
				$user = $users['username'];
				mysql_query("UPDATE user SET status=1 WHERE username='".$user."'");
			}
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function checkLogin($username, $password){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function updatePassword($username, $password, $newpassword){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			mysql_query("UPDATE user SET password='".$newpassword."' WHERE username='".$username."' AND password='".$password."'");
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function registerShelf($ShelfId, $password){	
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$checkShelf = mysql_query("SELECT ShelfId FROM shelf WHERE ShelfId='".$ShelfId."'");
			
			if(mysql_num_rows($checkShelf) != 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			mysql_query("INSERT INTO shelf (ShelfId, password) VALUES ('".$ShelfId."', '".$password."')");
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function associateShelf($username, $password, $ShelfId, $shelfPassword){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$shelfAuth = $DB_Connect->shelfAuth($ShelfId, $shelfPassword);
			
			if($shelfAuth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$checkAssociate = mysql_query("SELECT ShelfId FROM shelfUserMapping WHERE ShelfId='".$ShelfId."'");
			
			if(mysql_num_rows($checkShelf) != 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			mysql_query("INSERT INTO shelfUserMapping (username, ShelfId) VALUES ('".$username."', '".$ShelfId."')");
		}
		
		public function clearInventory($username, $password){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			mysql_query("DELETE FROM inventory WHERE username='".$username."'");
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
	}
	
	class Keygen {
		function generateRandomSecret() {
			$length = 6;
			$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
			$charactersLength = strlen($characters);
			$randomString = '';
			for ($i = 0; $i < $length; $i++) {
				$randomString .= $characters[rand(0, $charactersLength - 1)];
			}
			return $randomString;
		}
	}
  
	class Format {
		public function addInventoryArray($array, $InitialAmount, $CurrentAmount, $Barcode, $CategoryId, $DateAdded, $ExpiryDate, $Name, $Price, $ShelfRegion, $Status){
			$a = array("InitialAmount"=>$InitialAmount, "CurrentAmount"=>$CurrentAmount, "Barcode"=>$Barcode, "CategoryId"=>$CategoryId, "DateAdded"=>$DateAdded, "ExpiryDate"=>$ExpiryDate, "Name"=>$Name, "Price"=>$Price, "ShelfRegion"=>$ShelfRegion, "Status"=>$Status);
			
			array_push($array, $a);
			
			return $array;
		}
		
		public function addShelfArray($array, $ShelfId, $inventoryArray){
			$a = array("ShelfId" => $ShelfId, "Inventory" => $inventoryArray);
			
			array_push($array, $a);
			
			return $array;
		}
	}
  
	class DB_Connect {
		// Connecting to database
		public function connect() {
			/**
			 * Database config variables
			 */
			 define("DB_HOST", "mysql8.000webhost.com");
			 define("DB_USER", "a7164108_shelfe");
			 define("DB_PASSWORD", "waterloo24");
			 define("DB_DATABASE", "a7164108_shelfe");

			// connecting to mysql
			$con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
			// selecting database
			mysql_select_db(DB_DATABASE);

			// return database handler
			return $con;
		}

		// Closing database connection
		public function close() {
			mysql_close();
		}
		
		// Authenticate
		public function auth($username, $password){
			$result = mysql_query("SELECT username FROM user WHERE username = '".$username."' AND password = '".$password."' AND status = '1'") or die(mysql_error());
			
			return mysql_num_rows($result);
		}
		
		public function shelfAuth($ShelfId, $password){
			$result = mysql_query("SELECT ShelfId FROM shelf WHERE ShelfId = '".$ShelfId."' AND password = '".$password."'") or die(mysql_error());
			
			return mysql_num_rows($result);
		}
	}

	$rest = new RestServer();
	$rest->addServiceClass(Service);
	$rest->handle();
	
	exit;
?>
