<?php
    include("controleurs/c_Sommaire.php");
    
    // CONTROLES
    if(!isset($_REQUEST['action'])){
        $_REQUEST['action'] = 'default';
    }
    
    $_REQUEST['role_util'] = $_SESSION['role_vis'];

if(!isset($_REQUEST['role_util']))
{
	$_REQUEST['role_util'] = 'default';
}
$role_util = $_REQUEST['role_util'];

$localite = $_SESSION['localite'];

switch($role_util)
{
    case('Visiteur'):{
        $message = "Vous n&apos;êtes pas autorisé à consulter de statistiques en tant que visiteur.<br>En cas d&apos;erreur, consulter l&apos;administrateur.";
        include("vues/v_Message.php"); 
        break;
    }
    case('Délégué'):{
        $_SESSION['STAT_VISITE'] = $pdo->getNbVisitesRegion($localite);
        $_SESSION['STAT_MEDECIN'] = $pdo->getNbMedecinsRegion($localite);
        $_SESSION['STAT_VISITEURS'] = $pdo->getNbVisiteursRegion($localite);
        $_SESSION['STAT_DELEGUES'] = $pdo->getNbDeleguesRegion($localite);
        $_SESSION['STAT_RESPONSABLES'] = $pdo->getNbResponsablesRegion($localite);
        $_SESSION['STAT_VISITE_POURCENT'] = round(($_SESSION['STAT_VISITE'] / $pdo->getTotalVisites()) * 100, 2);
        $_SESSION['STAT_MEDECIN_POURCENT'] = round(($_SESSION['STAT_MEDECIN'] / $pdo->getTotalMedecins()) * 100, 2);
        $_SESSION['STAT_VISITEURS_POURCENT'] = round(($_SESSION['STAT_VISITEURS'] / $pdo->getTotalVisiteurs()) * 100, 2);
        $_SESSION['STAT_DELEGUES_POURCENT'] = round(($_SESSION['STAT_DELEGUES'] / $pdo->getTotalDelegues()) * 100, 2);
        $_SESSION['STAT_RESPONSABLES_POURCENT'] = round(($_SESSION['STAT_RESPONSABLES'] / $pdo->getTotalResponsables()) * 100, 2);
        include("vues/v_Statistiques.php");
        break;
    }
    case('Responsable'):{
        $_SESSION['STAT_VISITE'] = $pdo->getNbVisitesSecteur($localite);
        $_SESSION['STAT_MEDECIN'] = $pdo->getNbMedecinsSecteur($localite);
        $_SESSION['STAT_VISITEURS'] = $pdo->getNbVisiteursSecteur($localite);
        $_SESSION['STAT_DELEGUES'] = $pdo->getNbDeleguesSecteur($localite);
        $_SESSION['STAT_RESPONSABLES'] = $pdo->getNbResponsablesSecteur($localite);
        $_SESSION['STAT_VISITE_POURCENT'] = round(($_SESSION['STAT_VISITE'] / $pdo->getTotalVisites()) * 100, 2);
        $_SESSION['STAT_MEDECIN_POURCENT'] = round(($_SESSION['STAT_MEDECIN'] / $pdo->getTotalMedecins()) * 100, 2);
        $_SESSION['STAT_VISITEURS_POURCENT'] = round(($_SESSION['STAT_VISITEURS'] / $pdo->getTotalVisiteurs()) * 100, 2);
        $_SESSION['STAT_DELEGUES_POURCENT'] = round(($_SESSION['STAT_DELEGUES'] / $pdo->getTotalDelegues()) * 100, 2);
        $_SESSION['STAT_RESPONSABLES_POURCENT'] = round(($_SESSION['STAT_RESPONSABLES'] / $pdo->getTotalResponsables()) * 100, 2);
        include("vues/v_Statistiques.php");
        break;
    }
    default:{
        $message = "Vous n&apos;êtes pas autorisé à consulter de statistiques en tant que visiteur.<br>En cas d&apos;erreur, consulter l&apos;administrateur.";
        include("vues/v_Message.php"); 
        break;
    }
}
?>