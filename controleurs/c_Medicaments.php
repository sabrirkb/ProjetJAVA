<?php
include("vues/v_sommaire.php");
include("vues/v_lesMedicaments.php");

if(!isset($_REQUEST['action'])){
	$_REQUEST['action'] = 'consulterMedicaments';
}
$action = $_REQUEST['action'];
switch($action){
    case 'suivant':
        {
            break;
        }
    default: 
        {
            $lesMedicaments = $pdo->getLesMedicaments(0);
            foreach ($lesMedicaments as $unMedicament)
            {
				
            }
        }
    }
        
?>
