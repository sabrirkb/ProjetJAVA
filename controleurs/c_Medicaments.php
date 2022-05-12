<?php
include("vues/v_sommaire.php");
include("vues/v_lesMedicaments.php");

if(!isset($_REQUEST['action'])){
	$_REQUEST['action'] = 'default';
}

$action = $_REQUEST['action'];

if(isset($_SESSION['index']))
{
    $index = $_SESSION['index'];
}
else
{
    $index = 0;
}                            
switch($action){
    case 'suivant':
        {
            $index = $index + 1;
            if ($index <= $pdo->getMaxMedicaments())
            {
                $lesMedicaments = $pdo->getLesMedicaments($index);
                foreach($lesMedicaments as $unMedicament)
                {
                    $_SESSION['Med_depot'] = $unMedicament['MED_DEPOTLEGAL'];
                    $_SESSION['Med_nomMed'] = $unMedicament['MED_NOMCOMMERCIAL'];
                    $_SESSION['Med_famille'] = $unMedicament['FAM_CODE'];
                    $_SESSION['Med_compo'] = $unMedicament['MED_COMPOSITION'];
                    $_SESSION['Med_effets'] = $unMedicament['MED_EFFETS'];
                    $_SESSION['Med_contre'] = $unMedicament['MED_CONTREINDIC'];
                    $_SESSION['Med_prix'] = $unMedicament['MED_PRIXECHANTILLON'];
                }
            }
            else
            {
                $index = $pdo->getMaxMedicaments();
                $lesMedicaments = $pdo->getLesMedicaments($index);
                foreach($lesMedicaments as $unMedicament)
                {
                    $_SESSION['Med_depot'] = $unMedicament['MED_DEPOTLEGAL'];
                    $_SESSION['Med_nomMed'] = $unMedicament['MED_NOMCOMMERCIAL'];
                    $_SESSION['Med_famille'] = $unMedicament['FAM_CODE'];
                    $_SESSION['Med_compo'] = $unMedicament['MED_COMPOSITION'];
                    $_SESSION['Med_effets'] = $unMedicament['MED_EFFETS'];
                    $_SESSION['Med_contre'] = $unMedicament['MED_CONTREINDIC'];
                    $_SESSION['Med_prix'] = $unMedicament['MED_PRIXECHANTILLON'];
                }
            }
            $_SESSION['index'] = $index;
            break;
        }
    case 'precedent':
        {
            $index = $index - 1;
            if ($index >= 0)
            {
                $lesMedicaments = $pdo->getLesMedicaments($index);
                foreach($lesMedicaments as $unMedicament)
                {
                    $_SESSION['Med_depot'] = $unMedicament['MED_DEPOTLEGAL'];
                    $_SESSION['Med_nom'] = $unMedicament['MED_NOMCOMMERCIAL'];
                    $_SESSION['Med_famille'] = $unMedicament['FAM_CODE'];
                    $_SESSION['Med_compo'] = $unMedicament['MED_COMPOSITION'];
                    $_SESSION['Med_effets'] = $unMedicament['MED_EFFETS'];
                    $_SESSION['Med_contre'] = $unMedicament['MED_CONTREINDIC'];
                    $_SESSION['Med_prix'] = $unMedicament['MED_PRIXECHANTILLON'];
                }
            }
            else
            {
                $index = 0;
                $lesMedicaments = $pdo->getLesMedicaments($index);
                foreach($lesMedicaments as $unMedicament)
                {
                    $_SESSION['Med_depot'] = $unMedicament['MED_DEPOTLEGAL'];
                    $_SESSION['Med_nom'] = $unMedicament['MED_NOMCOMMERCIAL'];
                    $_SESSION['Med_famille'] = $unMedicament['FAM_CODE'];
                    $_SESSION['Med_compo'] = $unMedicament['MED_COMPOSITION'];
                    $_SESSION['Med_effets'] = $unMedicament['MED_EFFETS'];
                    $_SESSION['Med_contre'] = $unMedicament['MED_CONTREINDIC'];
                    $_SESSION['Med_prix'] = $unMedicament['MED_PRIXECHANTILLON'];
                }
            }
            $_SESSION['index'] = $index;
            break;
        }
    default: 
        {
            $_SESSION['index'] = 0;
            $index = 0;
            $lesMedicaments = $pdo->getLesMedicaments($index);
            foreach($lesMedicaments as $unMedicament)
            {
                $depot = $unMedicament['MED_DEPOTLEGAL'];
                $nom = $unMedicament['MED_NOMCOMMERCIAL'];
                $famille = $unMedicament['FAM_CODE'];
                $compo = $unMedicament['MED_COMPOSITION'];
                $effets = $unMedicament['MED_EFFETS'];
                $contre = $unMedicament['MED_CONTREINDIC'];
                $prix = $unMedicament['MED_PRIXECHANTILLON'];
            }
            break;
        }
    }

?>
