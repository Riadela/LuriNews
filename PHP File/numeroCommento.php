<?php

$response = array();


if (isset($_POST['IDNotizia'])) {

  $IDNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $result = mysql_query("SELECT COUNT(*) as NumeroCommento FROM Commento WHERE IDNotizia = '$IDNotizia'");
  
  
  
  
   
      if (mysql_num_rows($result) >= 0) {
      
      while($row = mysql_fetch_array($result))
     	$numeroCommento = $row["NumeroCommento"];
        
          $response["success"] = $numeroCommento;
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