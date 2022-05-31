<?php


include("controleurs/c_Sommaire.php");

if(!isset($_REQUEST['action'])){
	$_REQUEST['action'] = 'default';
}

$action = $_REQUEST['action'];

if(isset($_SESSION['Prat_index']))
{
    $index = $_SESSION['Prat_index'];
}
else
{
    $index = 1;
}                            
switch($action){
    //////////////////// PARTIE SAISIE DU SWTICH /////////////////////
    // Envoie sur la vue saisirCR permettant la saisie d'un CR

    case 'saisie':
        {
            include("vues/v_saisiePraticien.php");
            break;
        }

        case 'validerSaisiePraticien':
            {
              $numero=$_POST ['num'];
              $nom= $_POST['nom'];
              $prenom = $_POST['prenom'];
              $adresse = $_POST['adresse'];
              $CP= $_POST['cp'];
              $ville=$_POST['ville'];
              $coef=$_POST['coef'];
              $type=$_POST['choix_typePraticien'];
        $lePRA=$pdo->AjouterPraticien($numero, $nom, $prenom, $adresse, $CP, $ville, $coef, $type);
        $message = "Praticien ajouté avec succès.";
        include("vues/v_Message.php"); 
        break;
            }
    case'lesPraticiens': 
        {
            $_SESSION['Prat_index'] = 1;
            $index = 1;
            $lesPraticiens = $pdo->getLesPraticiens($index);
            foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
                include("vues/v_lesPraticiens.php");
            break;
        }

        ////////////////////// PARTIE AFFICHAGE DU SWITCH ///////////////////////

    case 'suivant':
        {
            $index = $index + 1;
            if ($index <= $pdo->getMaxPraticiens())
            {
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            else
            {
                $index = $pdo->getMaxPraticiens();
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            $_SESSION['Prat_index'] = $index;
            
            include("vues/v_lesPraticiens.php");
            break;
        }
    case 'precedent':
        {
            $index = $index - 1;
            if ($index >= 1)
            {
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            else
            {
                $index = 1;
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            $_SESSION['Prat_index'] = $index;
            
            include("vues/v_lesPraticiens.php");
            break;
        }
    case 'custom': 
        {
            $index = $_POST['updateIndex'];
            if ($index >= 1 and $index <= $pdo->getMaxPraticiens())
            {
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            if ($index < 1)
            {
                $index = 1;
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            if ($index > $pdo->getMaxPraticiens())
            {
                $index = $pdo->getMaxPraticiens();
                $lesPraticiens = $pdo->getLesPraticiens($index);
                foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
            }
            $_SESSION['Prat_index'] = $index;
            
            include("vues/v_lesPraticiens.php");
            break;
        }

    default: 
        {
            $_SESSION['Prat_index'] = 1;
            $index = 1;
            $lesPraticiens = $pdo->getLesPraticiens($index);
            foreach($lesPraticiens as $unPraticien)
                {
                    $_SESSION['Prat_num'] = $unPraticien['PRA_NUM'];
                    $_SESSION['Prat_nom'] = $unPraticien['PRA_NOM'];
                    $_SESSION['Prat_prenom'] = $unPraticien['PRA_PRENOM'];
                    $_SESSION['Prat_adresse'] = $unPraticien['PRA_ADRESSE'];
                    $_SESSION['Prat_cp'] = $unPraticien['PRA_CP'];
                    $_SESSION['Prat_ville'] = $unPraticien['PRA_VILLE'];
                    $_SESSION['Prat_coef'] = $unPraticien['PRA_COEFNOTORIETE'];
                    $_SESSION['Prat_codetype'] = $unPraticien['TYP_CODE'];
                }
                include("vues/v_lesPraticiens.php");
            break;
        }
    }


?>
