<main id="contenu">
     <h1>Rapports de visite – Saisie</h1>
<form action="index.php?uc=CompteRendu&action=validerSaisieCR" method="post">


<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->
  

      <table style="background-color: transparent">
      <tbody class="consultation">

      <!-- Création d'une zone de texte "matricule" -->
      <tr> <td> MATRICULE* </td>
      <td> <input style="margin: auto; width: 30%" type="text" name="matricule" required> </td> </tr>

      <!-- Création d'une zone de texte "numero de visite" -->
      <tr> <td> NUM&Eacute;RO* </td>
      <td> <input style="margin: auto; width: 30%" type="text" name="numVisite" required> </td> </tr>


      <!-- Création d'une zone de texte "Date visite" -->
      <tr> <td > DATE DE VISITE* </td>
      <td ><input style="margin: auto; width: 30%" type="date" name="dateVisite" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du praticien en "value", le nom et le prénom
du praticien donnent des détails sur le praticien-->
<tr> <td> <label for ="choix_praticien"> PRATICIEN*</td></label>
<td ><input list="praticiens" type="text" style="margin: auto; width: 50%" name="choix_praticien" required autocomplete = 'off'>
      <datalist id="praticiens">
      <?php 
      $lesPraticiens = $pdo->getLesPraticiens();
      foreach ($lesPraticiens as $unPraticien) {
                         $idPraticien = $unPraticien['PRA_NUM'];
                         $nomPraticien = $unPraticien['PRA_NOM'];
                         $prenomPraticien = $unPraticien['PRA_PRENOM'];
        
      echo '<option value='.$idPraticien.'&nbsp;-&nbsp;'.$nomPraticien.'&nbsp;'.$prenomPraticien.'></option>';
}
?>
</datalist> </td> </tr>


      <!-- Création d'une zone de texte "coefficient" -->
      <tr> <td > COEFFICIENT* </td>
      <td><input style="margin: auto; width: 30%" type="text" name="coef" required>  </td> </tr>


      <!-- Création d'une case à cocher "remplacant" -->
      <tr> <td > REMPLAÇANT* </td>
      <td><input style="margin: auto; width: 30%" type="checkbox" name="remplacant" required>  </td> 
      </tr>


      <!-- Création d'une liste déroulante "motif" -->
      <tr> <td > MOTIF* </td>
      <td ><input style="margin: auto; width: 30%" type="text" name="motif" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du médicament en "value", le nom du médicament -->
<tr> <td> <label for ="choix_produit"> PRODUITS PR&Eacute;SENT&Eacute;S*</td></label>
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
    <tr> <td> NOMBRE D'&Eacute;CHANTILLONS* </td>
    <td><input style="margin: auto; width: 10%" type="number" name="nbreEchantillon" title="Nombre" value="5" min="0" max="20" step="1" required=""> </td>
    </tr>


      <!-- Création d'une zone de texte "bilan" -->
      
      <tr> <td> <label for="bilan">BILAN</label> </td>

        <td> <textarea  name="bilan" rows="4" style="margin: auto; width: 100%""></textarea> </td>
      </tr>

      <!-- Création d'une case à cocher "documentation" -->
      <tr> <td> DOCUMENTATION OFFERTE* </td>
      <td><input style="margin: auto; width: 30%" type="checkbox" name="documentation" required>  </td> 
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