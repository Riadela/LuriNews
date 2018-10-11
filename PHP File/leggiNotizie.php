<?php

/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();

if(isset($_POST['numNotizie'])){

	$maxNotizie = $_POST['numNotizie'];
    $minNotizie = $maxNotizie - 10;
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
     
    // connecting to db
    $db = new DB_CONNECT();
    
    $result = mysql_query("SET CHARACTER SET utf8");
    
    
    $result = mysql_query("
    SELECT IDNotizia, IDUtente, titoloPrimo, testoPrimo, testoSecondo, CAST(dataPubblicato AS DATE) as dataNuova, dataPubblicato
	FROM Notizia
    WHERE numeroYes > numeroNo
    ORDER BY dataNuova DESC, numeroYes DESC, dataPubblicato DESC LIMIT $minNotizie, $maxNotizie") or die(mysql_error());
    
    // check for empty result
    if (mysql_num_rows($result) > 0) {
        // looping through all results
        
        $response["notizie"] = array();
        
        
        
        while ($row = mysql_fetch_array($result)) {
            // temp user array
            $notizia = array();
            $notizia["IDUtente"] = $row["IDUtente"];
            $notizia["IDNotizia"] = $row["IDNotizia"];
            $notizia["titoloPrimo"] = $row["titoloPrimo"];
            
            $notizia["testoPrimo"] = $row["testoPrimo"];
            $notizia["testoSecondo"] = $row["testoSecondo"];
            $notizia["dataPubblicato"] = $row["dataPubblicato"];
            
            
            $IDNotizia = $row["IDNotizia"];
     		$numeroInteressante = mysql_query("SELECT COUNT(*) as numeroInteressante FROM Interessante WHERE IDNotizia = '$IDNotizia'");
            $numeroYes = mysql_query("SELECT COUNT(*) as numeroYes FROM Valutazione WHERE IDNotizia = '$IDNotizia' AND YesNo = 'Yes' ");
            $numeroNo = mysql_query("SELECT COUNT(*) as numeroNo FROM Valutazione WHERE IDNotizia = '$IDNotizia' AND YesNo = 'No' ");
            $numeroCommento = mysql_query("SELECT COUNT(*) as numeroCommento FROM Commento WHERE IDNotizia = '$IDNotizia'");
            
            $rowInteressante = mysql_fetch_array($numeroInteressante);  
            $rowYes = mysql_fetch_array($numeroYes);  
            $rowNo = mysql_fetch_array($numeroNo);  
            $rowCommento = mysql_fetch_array($numeroCommento);  
            
            $notizia["numeroInteressante"] = $rowInteressante["numeroInteressante"];
            $notizia["numeroYes"] = $rowYes["numeroYes"];
            $notizia["numeroNo"] = $rowNo["numeroNo"];
            $notizia["numeroCommento"] = $rowCommento["numeroCommento"];
            
           
              // push single product into final response array
              array_push($response["notizie"], $notizia);
        }
        
        // success
        $response["success"] = 1;
          // echoing JSON response
        echo json_encode($response);
        
        } else {
        
        $response["success"] = 0;
        $response["message"] = "Nessuna notizia trovata";
     
        // echo no users JSON
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