<?php

$response = array();


if (isset($_POST['IDUtente']) && isset($_POST['IDNotizia'])) {

  $IDUtente = $_POST['IDUtente'];
  $IDNotizia = $_POST['IDNotizia'];
  
  
  // include db connect class
      require_once __DIR__ . '/db_connect.php';
  // connecting to db
      $db = new DB_CONNECT();      
      
     
     $result = mysql_query("SELECT YesNo FROM Valutazione WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IDNotizia' ");
     $row = mysql_fetch_array($result);
     $YesNo = $row["YesNo"];
     
     
     $result = mysql_query("DELETE FROM Valutazione  WHERE IDUtente = '$IDUtente' AND IDNotizia = '$IDNotizia'");
     
     if($result){
             
       $response["success"] = 1;
       $response["message"] = "Eccellente!";
       
       if($YesNo == "No"){
       $result = mysql_query("UPDATE `Notizia` 
                    SET numeroNo = numeroNo - 1 WHERE IDNotizia = '$IDNotizia'");}
       else if($YesNo == "Yes"){
       $result = mysql_query("UPDATE `Notizia` 
                    SET numeroYes = numeroYes - 1 WHERE IDNotizia = '$IDNotizia'");}
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
