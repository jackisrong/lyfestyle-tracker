<?php
    $con = oci_connect("XXXXX", "XXXXX", "XXXXX");
    if ($con) {
		//echo "Successfully connected to Oracle.<br>\n";
	} else {
		$err = oci_error();
		echo "Oracle Connect Error " . $err['message'] . "<br>\n";
    }

    $query_type = $_GET['query_type'];
    $columns = $_GET['columns'];
    $table = $_GET['table'];

    $s;
    if ($query_type == 'select' && $columns == 'all') {
        $s = oci_parse($con, 'SELECT * FROM ' . $table);
    } else if ($query_type == 'insert') {
        $s = oci_parse($con, 'INSERT INTO ' . $table . ' VALUES ' . '<todo - insert values>');
    } else if ($query_type == 'describe') {
        $s = oci_parse($con, 'SELECT * FROM ' . $table);
        oci_execute($s, OCI_DESCRIBE_ONLY);
        $ncols = oci_num_fields($s);
        $resultArray = array();
        for ($i = 1; $i <= $ncols; $i++) {
            $tempArray = [
                oci_field_name($s, $i) => oci_field_type($s, $i),
            ];
            array_push($resultArray, $tempArray);
        }
        echo json_encode($resultArray);
        oci_free_statement($s);
        oci_close($con);
        exit();
    }


    oci_execute($s, OCI_DEFAULT);

    $resultArray = array();
    $tempArray = array();

    while ($row = oci_fetch_object($s)) {
        $tempArray = $row;
        array_push($resultArray, $tempArray);
        //echo $row->TABLE_NAME . "<br>\n";
    }

    echo json_encode($resultArray);

    oci_commit($con);
    oci_free_statement($s);
    oci_close($con);
?>