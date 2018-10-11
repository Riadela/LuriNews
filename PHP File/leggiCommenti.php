<?php


$response = array();

if(isset($_POST['numCommenti']) &&
isset($_POST['IDNotizia'])){

	$maxCommenti = $_POST['numCommenti'];
    $minCommenti = $maxCommenti - 10;
    $IDNotizia = $_POST['IDNotizia'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
     
    // connecting to db
    $db = new DB_CONNECT();
    
    
    $result = mysql_query("
    SELECT IDUtente, testo, dataCommento
	FROM Commento	
    WHERE IDNotizia = '$IDNotizia'
    ORDER BY `Commento`.`dataCommento` ASC LIMIT $minCommenti, $maxCommenti") or die(mysql_error());
    
    // check for empty result
    if (mysql_num_rows($result) > 0) {
        // looping through all results
        // products node
        $response["commenti"] = array();
        
        while ($row = mysql_fetch_array($result)) {
            // temp user array
            $notizia = array();
            $notizia["IDUtente"] = $row["IDUtente"];
            $notizia["testo"] = $row["testo"];   
            $notizia["dataCommento"] = $row["dataCommento"];  
     
            // response array
            array_push($response["commenti"], $notizia);
      	}
        
        // success
        $response["success"] = 1;
          // echoing JSON response
        echo json_encode($response);
        
   } else {
        
        $response["success"] = 0;
        $response["message"] = "Nessuna notizia trovata";
     
        
        echo json_encode($response);
    }
  }else{
  	// required field is missing
    $response["success"] = 1;
    $response["message"] = "Campi mancanti!";
 
    // echoing JSON response
    echo json_encode($response);
  }
?>