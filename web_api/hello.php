<?php
    $con = oci_connect("XXXXX", "XXXXX", "XXXXX");
    if ($con) {
		//echo "Successfully connected to Oracle.<br>\n";
	} else {
		$err = oci_error();
		echo "Oracle Connect Error " . $err['message'] . "<br>\n";
    }

/*
    echo "inserting<br>\n";
    $stmt = oci_parse($con, "insert into titles values ('abc', 'TITLE', 'business', null, 2045, 'we3', '234', 'y', 'wnewew', 'e')");
    oci_execute($stmt, OCI_DEFAULT);
    echo "done<br>\n";
*/
    $param = $_GET['query_type'];

    //if (preg_match('/all_from/', $param))

    if (preg_match('/all_from/', $param)) {
        $table_name = preg_split("/all_from_/", $param)[1];

        $stmt = oci_parse($con, 'SELECT * FROM ' . $table_name);
        oci_execute($stmt, OCI_DEFAULT);

        $resultArray = array();
        $tempArray = array();

        while ($row = oci_fetch_object($stmt)) {
            $tempArray = $row;
            array_push($resultArray, $tempArray);
            //echo $row->TABLE_NAME . "<br>\n";
        }

        echo json_encode($resultArray);

        oci_commit($con);
        oci_free_statement($stmt);
        oci_close($con);
    }
?>