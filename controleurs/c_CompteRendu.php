<?php
include("vues/v_sommaire.php");
$action = $_REQUEST['action'];
$idVisiteur = $_SESSION['vis_matricule'];
switch($action){ 
	case 'validerSaisieCR':{
		include("vues/v_menu.php");   
        $raisonsociale = $_POST['raisonsociale'];
        $adresse = $_POST['adresse'];
        $codepostal = $_POST['codepostal'];
        $ville = $_POST['ville'];
        $tel = $_POST['telephone'];
        $fax = $_POST['fax'];
        $mail = $_POST['mail'];
        $idstatut = $_POST['choix_statut'];
        $lesEntreprises=$pdo->AjouterEntreprise($raisonsociale, $adresse, $codepostal, 
                                                $ville, $tel, $fax, $mail, $idstatut);
        $message = "Entreprise ajoutée";
        include("vues/v_Message.php");
        break;
	}
	
	}

?>