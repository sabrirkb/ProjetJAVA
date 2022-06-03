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

switch($role_util)
{
    case('Visiteur'):{
        echo '<div id="contenu"><h2 style="height: 15%">Vous n&apos;êtes pas autorisé à consulter de statistiques en tant que visiteur.<br>En cas d&apos;erreur, consulter l&apos;administrateur.</h2></div>'; 
        break;
    }
    case('Délégué'):{
        
        break;
    }
    case('Responsable'):{
        
        break;
    }
    default:{
        echo '<div id="contenu"><h2 style="height: 15%">Vous n&apos;êtes pas autorisé à consulter de statistiques en tant que visiteur.<br>En cas d&apos;erreur, consulter l&apos;administrateur.</h2></div>'; 
        break;
    }
}
    include("vues/v_Statistiques.php");
?>