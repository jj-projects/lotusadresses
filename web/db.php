<?php
// change the following number to the name/number of the file in the same directory as this script
$theCurrentVersion = "<db>";

// check if getting called for the version number or the actual file
// by checking if the parameter is being passed in
$updateWanted = $_GET["update_version"];
if ( empty($updateWanted) ) {
	 echo "v" . $theCurrentVersion;
}
else
	if ( file_exists("./".$updateWanted) ) {
		$theUpdate = file_get_contents($updateWanted);
		echo $theUpdate;
	}
	else {
		echo 'ERROR, file not found: '. $updateWanted . ".";
	}
?>