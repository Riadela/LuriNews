<?php
$response = array();


if (isset($_POST['IDUtente']) && 
isset($_POST['IDNotizia'])) {

  $IDUtente = $_POST['IDUtente'];
  $IdNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $result = mysql_query("DELETE FROM `Interessante` WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IdNotizia'");
  
  
  
  
   // check if row inserted or not
      if ($result) {
      
      
          $response["success"] = 1;
          $response["message"] = "Eccellente!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          }
      
        else{
        
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