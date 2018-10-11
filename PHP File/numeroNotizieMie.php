<?php

$response = array();


if (isset($_POST['IDUtente'])) {

  $IDUtente = $_POST['IDUtente'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $result = mysql_query("SELECT COUNT(*) as numeroNotizie FROM Notizia WHERE IDUtente = '$IDUtente'");
  
  
  
  
   
      if (mysql_num_rows($result) >= 0) {
      
      while($row = mysql_fetch_array($result))
     	$numeroNotizie = $row["numeroNotizie"];
        
          $response["success"] = $numeroNotizie;
          $response["message"] = "FATTO";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          }
      
    
    }else {
    
    // required field is missing
    $response["success"] = -1;
    $response["message"] = "Errore!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>