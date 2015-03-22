<?php
$print = <<<HTML
	<html>
		<head>
		  <meta charset="utf-8">
		  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		  <title>Shelf-e Login</title>
		  <link rel="stylesheet" href="css/style.css">
		</head>
		<body>
			<form action="graphicalView.php" method="post" class="login">
				<p>
					<label for="username">username:</label>
					<input type="text" name="username">
				</p>
				<p>
					<label for="password">password:</label> 
					<input type="password" name="password">
				</p>
				<p class="login-submit">
					<button type="submit" class="login-button">Login</button>
				</p>
			</form>
		</body>
	</html>
HTML;

echo $print;
?>