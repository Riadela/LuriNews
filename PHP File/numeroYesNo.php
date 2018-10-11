<?php

$response = array();


if (isset($_POST['IDNotizia'])) {

  $IDNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
  $resultYes = mysql_query("SELECT COUNT(*) as numeroYes FROM Valutazione WHERE IDNotizia = '$IDNotizia' AND YesNo = 'Yes' ");
  $resultNo = mysql_query("SELECT COUNT(*) as numeroNo FROM Valutazione WHERE IDNotizia = '$IDNotizia' AND YesNo = 'No' ");   
  
      if (mysql_num_rows($resultYes) >= 0 && mysql_num_rows($resultNo) >= 0) {      
     	
         $row = mysql_fetch_array($resultYes);        
     	 $response["numeroYes"] = (int)$row["numeroYes"];
         $numYes = (int)$row["numeroYes"];
         $result = mysql_query("UPDATE `Notizia` 
                    SET numeroYes = $numYes WHERE IDNotizia = '$IDNotizia'");
         
         $row = mysql_fetch_array($resultNo);
     	$response["numeroNo"] = (int)$row["numeroNo"];
        $numNo = (int)$row["numeroNo"];
        $result = mysql_query("UPDATE `Notizia` 
                    SET numeroNo = $numNo WHERE IDNotizia = '$IDNotizia'");
         
         $response["success"] = 0;
         $response["message"] = "Successo!";
   		
          // echoing JSON response
          echo json_encode($response);
          
         
          //echo json_encode($numeroYes);         
          //echo json_encode($numeroNo);
          
         
          }
      
    
    }else {
    
    // required field is missing
    $response["success"] = -1;
    $response["message"] = "Errore!";
 
    // echoing JSON response
    echo json_encode($response);
}
?>