<?php
	require_once "RestServer/RestServer.php";

        header('Content-type: application/json');
  
	class Service {
		public function itemIn($username, $password, $ShelfId, $ShelfRegions, $Amount){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			$ShelfRegions = explode(",", $ShelfRegions);
			
			$checkInitial = mysql_query("SELECT InitialAmount FROM inventory WHERE username='".$username."' AND ShelfId='-1' AND ShelfRegion='-1' AND InitialAmount='-1'");
			
			//New Item Added
			if(mysql_num_rows($checkInitial) != 0){
				$getDataQuery = mysql_query("SELECT * FROM inventory WHERE username='".$username."' AND ShelfId='-1' AND ShelfRegion='-1' AND InitialAmount='-1' LIMIT 1");
				
				while($getData = mysql_fetch_array($getDataQuery)){
					$Barcode = $getData['Barcode'];
					$CategoryId = $getData['CategoryId'];
					$DateAdded = $getData['DateAdded'];
					$ExpiryDate = $getData['ExpiryDate'];
					$Name = $getData['Name'];
					$Price = $getData['Price'];
				}
				
				for($i = 0; $i < sizeof($ShelfRegions); $i++){
					mysql_query("UPDATE inventory SET InitialAmount='".$Amount."', CurrentAmount='".$Amount."', ShelfId='".$ShelfId."', ShelfRegion='".$ShelfRegions[$i]."', Status='0' WHERE username='".$username."' AND ShelfId='-1' AND ShelfRegion='-1' AND InitialAmount='-1'");
					
					if($i < (sizeof($ShelfRegions)-1)){
						mysql_query("INSERT INTO inventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".$DateAdded."', '".$ExpiryDate."', '".$Name."', '".$Price."')");
					}
				}
			}
			//Item checked back in
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
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function itemOut($username, $password, $ShelfId, $ShelfRegions){
			$curOut = 1;
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
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
			
			$DB_Connect->close();
			return array("Success" => "true");
		}
		
		public function addItem($username, $password, $Barcode, $CategoryId, $ExpiryDate, $Name, $Price, $Override){
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			$Format = new Format();
			$ret = array();
			
			$auth = $DB_Connect->auth($username, $password);
			
			if($auth == 0){
				$DB_Connect->close();
				return array("Authentication" => "Failed");
			}
			
			if($Override != "true"){
				$checkItemOut = mysql_query("SELECT ShelfId FROM inventory WHERE username='".$username."' AND Status>'0'");
				
				if(mysql_num_rows($checkShelfRegion) > 0){
					return array("Success" => "false", "Error" => "ItemOut");
				}
			}
			
			$checkShelfRegion = mysql_query("SELECT ShelfId FROM inventory WHERE username='".$username."' AND ShelfId='-1' AND ShelfRegion='-1' AND InitialAmount='-1'");
			
			if(mysql_num_rows($checkShelfRegion) == 0){
				mysql_query("INSERT INTO inventory (username, Barcode, CategoryId, DateAdded, ExpiryDate, Name, Price) VALUES ('".$username."', '".$Barcode."', '".$CategoryId."', '".time()."', '".$ExpiryDate."', '".$Name."', '".$Price."')");
			}
			else{
				$DB_Connect->close();
				return array("Success" => "false", "Error" => "MissingItem");
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
		
		public function newUser($username, $password, $secret){
			if($secret != "a1b2c3d4e5f6"){
				return array("Authentication" => "Failed"); 
			}
			
			$DB_Connect = new DB_Connect();
			$con = $DB_Connect->connect();
			
			$checkUser = mysql_query("SELECT username FROM user WHERE username='".$username."'");
			
			if(mysql_num_rows($checkUser) != 0){
				$DB_Connect->close();
				return array("Success" => "false");
			}
			
			mysql_query("INSERT INTO user (username, password) VALUES ('".$username."', '".$password."')");
			
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
			 define("DB_HOST", "mysql1.000webhost.com");
			 define("DB_USER", "a5651018_shelfe");
			 define("DB_PASSWORD", "fydp24");
			 define("DB_DATABASE", "a5651018_shelfe");

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
			$result = mysql_query("SELECT username FROM user WHERE username = '".$username."' AND password = '".$password."'") or die(mysql_error());
			
			return mysql_num_rows($result);
		}
	}

	$rest = new RestServer();
	$rest->addServiceClass(Service);
	$rest->handle();
	
	exit;
?>
