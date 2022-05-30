<main id="contenu">
     <h1>Saisir un nouveau rapport</h1>
<form action="index.php?uc=gererCR&action=validerSaisieCR" method="post">

<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->
  

      <table style="background-color: transparent">
      <tbody class="consultation">

      <!-- Création d'une zone de texte "matricule" -->
      <tr> <td> MATRICULE* </td>
      <td> <input style="margin: auto; width: 30%" type="text" name="matricule" required> </td> </tr>

      <!-- Création d'une zone de texte "numéro rapport" -->

      <tr> <td><p>Numéro rapport :</p></td>
              <td><input type="number" name="num" value="<?=  $pdo->retournerLeDernierNumRap()+1; ?>" readonly>*</td>
      </tr>

      <!-- Création d'une zone de texte "Date visite" -->
      <tr> <td > DATE DE VISITE* </td>
      <td ><input style="margin: auto; width: 30%" type="date" name="dateVisite"  max="<?= date('Y-m-d'); ?>" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du praticien en "value", le nom et le prénom
du praticien donnent des détails sur le praticien-->
<tr> <td> <label for ="choix_praticien"> PRATICIEN*</td></label>
<td ><input list="praticiens" type="text" style="margin: auto; width: 50%" name="choix_praticien" required autocomplete = 'off'>
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

      <!-- Création d'une case à cocher "remplacant" -->
      <tr> <td > REMPLAÇANT* </td>
      <td><input style="margin: auto; width: 30%" type="checkbox" name="remplacant">  </td> 
      </tr>


      <!-- Création d'une zone de texte "motif" -->
      <tr> <td > MOTIF DE VISITE* </td>
      <td ><input style="margin: auto; width: 30%" type="text" name="motif" required>  </td> </tr>

       <!-- liste pour sélectionner un premier produit -->
      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du  médicament en "value", le nom du médicament -->
<tr> <td> <label for ="choix_produit"> PRODUIT 1*</td></label>
<td><input style="margin: auto; width: 50%" list="produits" multiple="oui" type="text" name="choix_produit" autocomplete = 'off' required>
      <datalist id="produits">
      <?php 
      $lesProduits = $pdo->getLesProduits();
      foreach ($lesProduits as $unProduit) {
                         $idMedicament = $unProduit['MED_DEPOTLEGAL'];
                         $nomMedicament = $unProduit['MED_NOMCOMMERCIAL'];
        
      echo'<option value='.$idMedicament.'&nbsp;–&nbsp;'.$nomMedicament.'> </option>';
}
?>
</datalist> </td> </tr>

    <!-- Création d'un champ "nombre d'échantillons" -->
    <tr> <td> NOMBRE D'&Eacute;CHANTILLONS DU PRODUIT 1* </td>
    <td><input style="margin: auto; width: 10%" type="number" name="nbreEchantillon" title="Nombre" value="5" min="0" max="20" step="1" required> </td>
    </tr>
    

    <!-- liste pour sélectionner un deuxième produit -->
    <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du médicament en "value", le nom du médicament -->
<tr> <td> <label for ="choix_produit"> PRODUITS 2*</td></label>
<td><input style="margin: auto; width: 50%" list="produits" multiple="oui" type="text" name="choix_produit" autocomplete = 'off'>
      <datalist id="produits">
      <?php 
      $lesProduits = $pdo->getLesProduits();
      foreach ($lesProduits as $unProduit) {
                         $idMedicament = $unProduit['MED_DEPOTLEGAL'];
                         $nomMedicament = $unProduit['MED_NOMCOMMERCIAL'];
        
      echo'<option value='.$idMedicament.'&nbsp;–&nbsp;'.$nomMedicament.'> </option>';
}
?>
</datalist> </td> </tr>

    <!-- Création d'un champ "nombre d'échantillons" -->
    <tr> <td> NOMBRE D'&Eacute;CHANTILLONS* DU PRODUIT 2</td>
    <td><input style="margin: auto; width: 10%" type="number" name="nbreEchantillon" title="Nombre" value="5" min="0" max="20" step="1" required> </td>
    </tr>


      <!-- Création d'une zone de texte "bilan" -->
      
      <tr> <td> <label for="bilan">BILAN</label> </td>

        <td> <textarea  name="bilan" rows="4" style="margin: auto; width: 100%""></textarea> </td>
      </tr>

      <!-- Création d'une case à cocher "documentation" -->
      <tr> <td> DOCUMENTATION OFFERTE* </td>
      <td><input style="margin: auto; width: 30%" type="checkbox" name="documentation">  </td> 
      </tr>
</tbody>
      </table>

<!-- Aide pour l'utilisateur signifiant que les champs précédés d'une astérisque sont obligatoires -->
      
      <p>
      * : Champs obligatoire.
      </p>
      <input type="submit" name="Valider" value="Valider">  
      <input type="reset" name="Annuler" value="Annuler">  
      </form>
    </main>