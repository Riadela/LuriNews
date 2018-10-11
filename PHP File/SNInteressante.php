<?php

$response = array();


if (isset($_POST['IDUtente']) && 
isset($_POST['IDNotizia'])) {

  $IDUtente = $_POST['IDUtente'];
  $IDNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $result = mysql_query("SELECT * FROM Interessante WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IDNotizia'");
  
  
  
  
   // check if row inserted or not
      if (mysql_num_rows($result) == 0) {
      
     
          $response["success"] = 0;
          $response["message"] = "Non ha messo interessante!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          }
      
        else if(mysql_num_rows($result) > 0){
        // failed to insert row
          $response["success"] = 1;
          $response["message"] = "Ha gia messo interessante";
   
          // echoing JSON response
          echo json_encode($response);
          
      } 
    
    }else {
    
    // required field is missing
    $response["success"] = 2;
    $response["message"] = "Errore!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>