<?php
$print = <<<HTML
	<html>
		<body>
			<form action="graphicalView.php" method="post">
				username: <input type="text" name="username"><br>
				password: <input type="password" name="password"><br>
				<input type="submit">
			</form>
		</body>
	</html>
HTML;

echo $print;
?>