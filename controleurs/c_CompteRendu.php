<?php
include("vues/v_sommaire.php");
$action = $_REQUEST['action'];
switch($action){ 
    // Envoie sur la vue saisirCR permettant la saisie d'un CR
    case 'ajouterCR':{
	    include("vues/v_sommaire.php");
        $lesPraticiens=$pdo->getLesPraticiens();
        $lesProduits=$pdo->getLesProduits();
        include("vues/v_saisirCR.php");
	    break;
	}
        
	case 'validerSaisieCR':{
		include("vues/v_sommaire.php");   
        $Matricule = $_POST['matricule'];
        $dateVisite = $_POST['dateVisite'];
        $praticien = $_POST['choix_praticien'];
        $coef = $_POST['coef'];
        $remplacant = $_POST['remplacant'];
        $Echantillon= $_POST['nbreEchantillon'];
        $Produit= $_POST['documentation'];
        $bilan= $_POST['choix_produit'];
        $documentation= $_POST['documentation'];

        $leCR=$pdo->AjouterCR($Numero, $dateVisite, $praticien, $coef, $remplacant , $Echantillon, $Produit, $bilan, $documentation);
        $message = "Compte rendu ajouté avec succès";
       // include("vues/v_Message.php"); 
        break;
	}
  
	}


?>