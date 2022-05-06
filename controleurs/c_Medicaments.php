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
            echo $index;
            break;
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
            foreach ($lesMedicaments as $unMedicament)
            {
				echo $lesMedicaments[0]['med_depotlegal'];
            }
            
            echo "Pas d'action sélectionnée.";
        }
    }
        
?>
