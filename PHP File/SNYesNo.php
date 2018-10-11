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
      
  $result = mysql_query("SELECT * FROM Valutazione WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IDNotizia'");
  
  
  
  
   // check if row inserted or not
      if (mysql_num_rows($result) == 0) {
      
     
          $response["success"] = 0;
          $response["message"] = "Non ha messo YesNo!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          }
      
        else if(mysql_num_rows($result) > 0){
        
         $result = mysql_query("SELECT * FROM Valutazione WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IDNotizia' AND YesNo = 'Yes'");
         
         if(mysql_num_rows($result) > 0){
            $response["success"] = 1;
            $response["message"] = "Ha messo yes";   
            // echoing JSON response
            echo json_encode($response);}
         else{
           $response["success"] = 2;
           $response["message"] = "Ha messo no";   
           // echoing JSON response
            echo json_encode($response);}
          
      } 
    
    }else {
    
    // required field is missing
    $response["success"] = 3;
    $response["message"] = "Errore!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>