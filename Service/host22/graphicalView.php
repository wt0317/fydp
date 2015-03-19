<?php	
	echo "<html>";
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
	
	function usernameIsDefinedInRequest() {
		return array_key_exists("username", getRequestAttributeArray());
	}
	
	function passwordIsDefinedInRequest() {
		return array_key_exists("password", getRequestAttributeArray());
	}

	function getRequestAttributeArray() {
		return array_change_key_case($_REQUEST, CASE_LOWER);;
	}
	
	$baseTable = <<<HTML
			<head>
				<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
				<style>
					.grid{
						width: 60%;
						height: 90%;
						verticle-align: middle;
						text-align: center;
						border: solid black 1px;
						border-collapse: collapse;
						table-layout: fixed;
					}
					
					td{
						border: 1px solid black;
					}
					
					.label{
						width: 60%;
						text-align: center;
					}
				</style>
			</head>
			<body>
				<table class="grid" id="grid">
					<tr>
						<td id="0">0</td>
						<td id="5">5</td>
						<td id="10">10</td>
						<td id="15">15</td>
						<td id="20">20</td>
						<td id="25">25</td>
						<td id="30">30</td>
						<td id="35">35</td>
						<td id="40">40</td>
					</tr>
					<tr>
						<td id="1">1</td>
						<td id="6">6</td>
						<td id="11">11</td>
						<td id="16">16</td>
						<td id="21">21</td>
						<td id="26">26</td>
						<td id="31">31</td>
						<td id="36">36</td>
						<td id="41">41</td>
					</tr>
					<tr>
						<td id="2">2</td>
						<td id="7">7</td>
						<td id="12">12</td>
						<td id="17">17</td>
						<td id="22">22</td>
						<td id="27">27</td>
						<td id="32">32</td>
						<td id="37">37</td>
						<td id="42">42</td>
					</tr>
					<tr>
						<td id="3">3</td>
						<td id="8">8</td>
						<td id="13">13</td>
						<td id="18">18</td>
						<td id="23">23</td>
						<td id="28">28</td>
						<td id="33">33</td>
						<td id="38">38</td>
						<td id="43">43</td>
					</tr>
					<tr>
						<td id="4">4</td>
						<td id="9">9</td>
						<td id="14">14</td>
						<td id="19">19</td>
						<td id="24">24</td>
						<td id="29">29</td>
						<td id="34">34</td>
						<td id="39">39</td>
						<td id="44">44</td>
					</tr>
				</table>
				<div class="label">
					Front of Shelf
				</div>
			<body>
HTML;

	echo $baseTable;
	
	$DB_Connect = new DB_Connect();
	$con = $DB_Connect->connect();
	
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	$auth = $DB_Connect->auth($username, $password);
		
	if($auth == 0){
		$DB_Connect->close();
		$script = <<<SCRIPT
			<script>
				$('#grid').html("Invalid Username/Password");
			</script>
SCRIPT;
		echo $script;
	}
	else{
		$getShelfIds = mysql_query("SELECT Distinct(ShelfId) FROM inventory WHERE username='".$username."'");
		
		if(mysql_num_rows($getShelfIds) == 0){
			$DB_Connect->close();
			return array("Inventory" => "Empty");
		}

		while($ShelfIds = mysql_fetch_array($getShelfIds)) {
			$ShelfId = $ShelfIds['ShelfId'];
			
			$getItemList = mysql_query("SELECT DISTINCT Name FROM inventory WHERE username='".$username."' AND ShelfId='".$ShelfId."'");
			while($item = mysql_fetch_array($getItemList)){
				$getInventory = mysql_query("SELECT ShelfRegion FROM inventory WHERE username='".$username."' AND ShelfId='".$ShelfId."' AND Name='".$item[0]."'");
				while($id = mysql_fetch_array($getInventory)){
					echo "<script>$('#".$id[0]."').html('".$item[0]."');</script>";
				}
			}
		}
		
		$DB_Connect->close();
	}	
	
	echo "</html>";
?>
