<?php
include("vues/v_sommaire.php");
include("vues/v_lesMedicaments.php");

if(!isset($_REQUEST['action'])){
	$_REQUEST['action'] = 'consulterMedicaments';
}
$action = $_REQUEST['action'];
$index = 0;
switch($action){
    case 'suivant':
        {
            $index = $index + 1;
            $lesMedicaments = $pdo->getLesMedicaments($index);
            foreach($lesMedicaments as $unMedicament)
            {
                echo $unMedicament['MED_DEPOTLEGAL'];
			    $depot = $unMedicament['MED_DEPOTLEGAL'];
            }
        }
    case 'precedent':
        {
            $index = $index - 1;
            echo $index;
            break;
        }
    default: 
        {
            $lesMedicaments = $pdo->getLesMedicaments(0);
            foreach($lesMedicaments as $unMedicament)
            {
                echo $unMedicament['MED_DEPOTLEGAL'];
			    $depot = $unMedicament['MED_DEPOTLEGAL'];
            }
        }
    }
        
?>
