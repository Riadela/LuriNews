<?php

$response = array();


if (isset($_POST['IDNotizia'])) {


  $IDNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $result = mysql_query("DELETE FROM `Notizia` WHERE IDNotizia = '$IDNotizia'");
  
  
  
  
   // check if row inserted or not
      if ($result) {
      
     	$result = mysql_query("DELETE FROM `Interessante` WHERE IDNotizia = '$IDNotizia'");	
        $result = mysql_query("DELETE FROM `Valutazione` WHERE IDNotizia = '$IDNotizia'");
        $result = mysql_query("DELETE FROM `Commento` WHERE IDNotizia = '$IDNotizia'");
        
          $response["success"] = 1;
          $response["message"] = "Non ha messo interessante!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          }
      
        else {
        // failed to insert row
          $response["success"] = 2;
          $response["message"] = "Errore nell'eliminazione";
   
          // echoing JSON response
          echo json_encode($response);
          
      } 
    
    }else {
    
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Errore!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>