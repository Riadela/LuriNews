<?php

$response = array();


if (isset($_POST['IDUtente']) && 
isset($_POST['password'])) {

  $IDUtente = $_POST['IDUtente'];
  $password = $_POST['password'];
  
    
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();
      
      // query per il login
  $result = mysql_query("SELECT IDUtente, password FROM `Utente` WHERE IDUtente = '$IDUtente' AND password = MD5('$password') ") or die(mysql_error());
  
  // check for empty result
  if (mysql_num_rows($result) > 0) {	
      
      
    $response["utenti"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $utente = array();
        $utente["IDUtente"] = $row["IDUtente"];
        $utente["password"] = $row["password"];        
 
        
        array_push($response["utenti"], $utente);
    }
    
    // success
    $response["success"] = 1;
      // echoing JSON response
    echo json_encode($response);     
      
  }else{
  	
    $response["success"] = 2;
    $response["message"] = "Nome utente o password errato";
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