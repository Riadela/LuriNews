<?php

$response = array();


if (isset($_POST['titoloPrimo']) && 
isset($_POST['testoPrimo']) &&
isset($_POST['testoSecondo'])&&
isset($_POST['IDUtente'])) {

  $titoloPrimo = mysql_real_escape_string($_POST['titoloPrimo']);
  $testoPrimo = mysql_real_escape_string($_POST['testoPrimo']);
  $testoSecondo = mysql_real_escape_string($_POST['testoSecondo']);
  $IDUtente = $_POST['IDUtente'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();
      
       
      
      $data = date('Y-m-d H:i:s');
  $result = mysql_query("INSERT INTO `Notizia`(`IDNotizia`, `titoloPrimo`, `testoPrimo`, `testoSecondo`, `dataPubblicato`,`IDUtente`, `numeroYes`, `numeroNo`) 
  VALUES (NULL, '$titoloPrimo', '$testoPrimo', '$testoSecondo', '$data', '$IDUtente', '0','0')");
  
  
  
  
   // check if row inserted or not
      if ($result) {
      
     
          $response["success"] = 1;
          $response["message"] = "Eccellente!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          } else{
        // failed to insert row
          $response["success"] = 2;
          $response["message"] = "Errore...:/";
   
          // echoing JSON response
          echo json_encode($response);
          
      } 
    
    }else {
    
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Campi mancanti!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>