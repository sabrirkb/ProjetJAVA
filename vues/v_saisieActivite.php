<main id="contenu">
     <h1>Saisir un nouveau rapport</h1>
<form action="index.php?uc=ac&action=validerSaisieActivite" method="post">

<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->
  

      <table style="background-color: transparent">
      <tbody class="consultation">

      !-- Création d'une zone de texte "matricule" -->
      <tr> <td> MATRICULE* </td>
      <td> <input style="margin: auto; width: 10%" type="text" name="matricule" value="<?php echo strtoupper($_SESSION['CR_matricule']);?>" required readonly> </td> </tr>

      <!-- Création d'une zone de texte "numéro AC" -->
      <tr> <td> NUM&Eacute;RO de l'AC* </td>
      <td> <input style="margin: auto; width: 10%" type="text" name="numAC" required readonly> </td> </tr>

      <!-- Création d'une zone de texte "Date AC" -->
      <tr> <td > DATE* </td>
      <td ><input style="margin: auto; width: 30%" type="date" name="dateAC"  max="<?= date('Y-m-d'); ?>" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du praticien en "value", le nom et le prénom
du praticien donnent des détails sur le praticien-->
<tr> <td> <label for ="choix_praticien"> PRATICIENS INVIT&Eacute;S*</td></label>
<td ><input list="praticiens" type="text" multiple="multiple" style="margin: auto; width: 50%" name="choix_praticien" required autocomplete = 'off'>
      <datalist id="praticiens">
      <?php 
      $lesPraticiens = $pdo->getPraticiens();
      foreach ($lesPraticiens as $unPraticien) {
                         $idPraticien = $unPraticien['PRA_NUM'];
                         $nomPraticien = $unPraticien['PRA_NOM'];
                         $prenomPraticien = $unPraticien['PRA_PRENOM'];
        
      echo '<option value='.$idPraticien.'&nbsp;-&nbsp;'.$nomPraticien.'&nbsp;'.$prenomPraticien.'></option>';
}
?>
</datalist> </td> </tr>

<!-- liste pour sélectionner le lieu -->
      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du lieu en "value", le nom du lieu -->
      <tr> <td> <label for ="choix_lieu"> Lieu de l'AC*</td></label>
<td><input style="margin: auto; width: 50%" list="lieux"  type="text" name="choix_lieu" autocomplete = 'off' required>
      <datalist id="lieux">
      <?php 
      $lesLieux= $pdo->getLesLieux();
      foreach ($lesLieux as $unLieu) {
                         $codeRegion = $unLieu['REG_CODE'];
                         $nomRegion = $unLieu['REG_NOM'];
        
      echo'<option value='.$codeRegion.'&nbsp;–&nbsp;'.$nomRegion.'> </option>';
}
?>
</datalist> </td> </tr>

      <!-- Création d'une zone de texte "thème" -->
      <tr> <td > TH&Eacute;ME DE l'AC* </td>
      <td ><input style="margin: auto; width: 30%" type="text" name="theme" required>  </td> </tr>
    

      <!-- Création d'une zone de texte "motif" -->
      
      <tr> <td> <label for="bilan">MOTIF*</label> </td>

        <td> <textarea  name="motif" rows="4" style="margin: auto; width: 100%" required></textarea> </td>
      </tr>

      
<!-- Aide pour l'utilisateur signifiant que les champs précédés d'une astérisque sont obligatoires -->
      
      <p>
      * : Champs obligatoire.
      </p>
      <input type="submit" name="Valider" value="Valider">  
      <input type="reset" name="Annuler" value="Annuler">  
      </form>
    </main>