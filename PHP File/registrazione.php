<?php

$response = array();


if (isset($_POST['IDUtente']) && 
isset($_POST['password']) &&
isset($_POST['email'])) {

  $IDUtente = $_POST['IDUtente'];
  $password = MD5($_POST['password']);
  $email = $_POST['email'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();
      
      $data = date('Y-m-d');
      
      $result = mysql_query("SELECT email FROM `Utente` WHERE email = '$email'");
      
      if(mysql_num_rows($result) == 0){
      
      $result = mysql_query("INSERT INTO `Utente`(`IDUtente`, `password`, `email`, `dataRegistrazione`) 
        VALUES ('$IDUtente','$password','$email','$data')");
        
        if ($result) {
            
            $response["success"] = 1;
            $response["message"] = "Registrazione effettuata!";         
         
            // echoing JSON response
            echo json_encode($response);
          }
          else{
          $response["success"] = 2;
          $response["message"] = "Utente gia esistente";
   
          // echoing JSON response
          echo json_encode($response);}
          
      } else{
      
      	$response["success"] = 3;
          $response["message"] = "Email gia esistente";
   
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