<?php

$response = array();


if (isset($_POST['IDUtente']) &&
isset($_POST['Password']) &&
isset($_POST['PasswordVecchia'])) {

  $IDUtente = $_POST['IDUtente'];
  $Password = $_POST['Password'];
  $PasswordVecchia = $_POST['PasswordVecchia'];

  
    
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();
      
      // query per il login
  $result = mysql_query("SELECT IDUtente FROM `Utente` WHERE IDUtente = '$IDUtente' AND password = '$PasswordVecchia'");
  
  // check for empty result
  if (mysql_num_rows($result) > 0) {
         
     $result1 = mysql_query("UPDATE `Utente` 
     SET `password`= '$Password' 
     WHERE IDUtente = '$IDUtente'");       
     
     
      if($result1){
        	// success
   		 $response["success"] = 1;
     	 // echoing JSON response
   		 echo json_encode($response); 
        
        }
     
     
     
    
          
     
  }else{
  	
    $response["success"] = 2;
    $response["message"] = "Password errata";
     // echoing JSON response
       echo json_encode($response);
    
  }
  }
  
else{
	
    $response["success"] = 0;
    $response["message"] = "Campi mancanti";
     // echoing JSON response
       echo json_encode($response);
    
}

?>