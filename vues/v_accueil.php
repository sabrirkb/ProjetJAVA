<div id="contenu">
    <h1>Application de gestion des visites GSB</h1>
    <h1 style="font-weight: normal">
        <br>
        Bienvenue dans votre espace,
	    <strong><?php echo $_SESSION['prenom']."  ".$_SESSION['nom']  ?></strong> !
        <br>
    </h1>
    <h3 style="font-weight: normal">
    Vous êtes connecté en tant que <?php echo strtolower($_SESSION['role_vis']);?>.
    <br>
    <?php echo ucfirst($_SESSION['STAT_TYPELIEU']) . " " . strtoupper($_SESSION['localite']); ?>.
    <br>
    <br>
    ⯇ Les actions que vous êtes autorisé(e) à effectuer sont représentées par les onglets du menu ci-contre.
        <br>
        <br>
    </h3>
    En cas d'erreur, merci de consulter le service administratif.
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
</div>
