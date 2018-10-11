<?php

$response = array();


if (isset($_POST['IDUtente']) &&
isset($_POST['IDUtenteVecchio'])) {

  $IDUtente = $_POST['IDUtente'];
  $IDUtenteVecchio = $_POST['IDUtenteVecchio'];

  
    
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();
      
      // query per il login
  $result = mysql_query("SELECT IDUtente FROM `Utente` WHERE IDUtente = '$IDUtenteVecchio'");
  
  // check for empty result
  if (mysql_num_rows($result) > 0) {	     
     
     $result = mysql_query("SELECT IDUtente FROM `Utente` WHERE IDUtente = '$IDUtente'");     
     
     if (mysql_num_rows($result) == 0){
     
     	 $result1 = mysql_query("UPDATE `Utente` 
     	SET `IDUtente`= '$IDUtente' 
     	WHERE IDUtente = '$IDUtenteVecchio'");
        
        $result2 = mysql_query("UPDATE `Commento` 
     	SET IDUtente = '$IDUtente' 
    	WHERE IDUtente = '$IDUtenteVecchio'");
        
        $result3 = mysql_query("UPDATE `Interessante` 
        SSET IDUtente = '$IDUtente' 
    	WHERE IDUtente = '$IDUtenteVecchio'");
        
        $result4 = mysql_query("UPDATE `Notizia` 
     	SET IDUtente = '$IDUtente' 
    	WHERE IDUtente = '$IDUtenteVecchio'");
        
        $result4 = mysql_query("UPDATE `Valutazione` 
     	SET IDUtente = '$IDUtente' 
    	WHERE IDUtente = '$IDUtenteVecchio'");
        
        
        if($result1){
        	// success
   		 $response["success"] = 1;
     	 // echoing JSON response
   		 echo json_encode($response); 
        
        }
     
     }else{
     	
        // success
   		 $response["success"] = 3;
     	 // echoing JSON response
   		 echo json_encode($response);  
     
     }
     
    
    
    
       
      
  }else{
  	
    $response["success"] = 2;
    $response["message"] = "Nome utente errato";
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