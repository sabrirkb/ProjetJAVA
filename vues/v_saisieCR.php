<form action="index.php?uc=CompteRendu&action=validerSaisieCR" method="post">
<div id="contenu">

<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->

      <h2>Rapport de visite</h2>

      <table class = "ajouter">


      <!-- Création d'une zone de texte "matricule" -->
      <tr> <td class = "ajouter"> Matricule: * </td>
      <td class = "ajouter"> <input size="22" type="text" name="matricule" required> </td> </tr>


      <!-- Création d'une zone de texte "Date visite" -->
      <tr> <td class = "ajouter"> Date Visite : * </td>
      <td class = "ajouter"><input size="22" type="date" name="dateVisite" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du praticien en "value", le nom et le prénom
du praticien donnent des détails sur le praticien-->
<tr> <td class = "ajouter"> <label for ="choix_praticien"> Praticien : *</td></label>
<td class = "ajouter"><input list="praticiens" type="text" name="choix_praticien" required autocomplete = 'off'>
      <datalist id="praticiens">
      <?php  foreach ($lesPraticiens as $unPraticien) {
                         $idPraticien = $unPraticien['PRA_NUM'];
                         $NomPraticien = $unPraticien['PRA_NOM'];
                         $PrenomPraticien = $unPraticien['PRA_PRENOM'];
        
      echo'<option value='.$idPraticien.' '.$nomPraticien.' '.$prenomPraticien.'</option>';
}
?>
</datalist> </td> </tr>


      <!-- Création d'une zone de texte "coefficient" -->
      <tr> <td class = "ajouter"> coefficient: * </td>
      <td class = "ajouter"><input size="22" type="text" name="coef" required>  </td> </tr>


      <!-- Création d'une case à cocher "remplacant" -->
      <tr> <td class = "ajouter"> Remplaçant : * </td>
      <td class = "ajouter"><input size="22" type="checkbox" name="remplacant" required>  </td> 
      </tr>


      <!-- Création d'une liste déroulante "motif" -->
      <tr> <td class = "ajouter"> Motif : * </td>
      <td class = "ajouter"><input size="22" type="text" name="motif" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du médicament en "value", le nom du médicament -->
<tr> <td class = "ajouter"> <label for ="choix_produit"> Produits présentés : *</td></label>
<td class = "ajouter"><input list="produits" multiple="oui" type="text" name="choix_produit" autocomplete = 'off'>
      <datalist id="produits">
      <?php  foreach ($lesProduits as $unProduit) {
                         $idMedicament = $unMedicament['MED_DEPOTLEGAL'];
                         $NomMedicament = $unMedicament['MED_NOMCOMMERCIAL'];
        
      echo'<option value='.$idMedicament.' '.$nomMedicament.' </option>';
}
?>
</datalist> </td> </tr>

    <!-- Création d'un champ "nombre d'échantillons" -->
    <tr> <td class = "ajouter"> Nombre d'échantillons : * </td>
    <td class = "ajouter"><input type="number" name="nbreEchantillon" title="Nombre" value="5" min="0" max="20" step="1" required=""> </td>
    </tr>


      <!-- Création d'une zone de texte "bilan" -->
      <tr> <td class = "ajouter"> Bilan: * </td>
      <td class = "ajouter"><input size="22" type="text" name="bilan" required>  </td> </tr>

      <tr> <td class = "ajouter"> <label for="bilan">Bilan : </label> </td>

        <td class = "ajouter"> <textarea  name="bilan" rows="4" cols="40"></textarea> </td>
      </tr>

      <!-- Création d'une case à cocher "documentation" -->
      <tr> <td class = "ajouter"> Documentation offerte : * </td>
      <td class = "ajouter"><input size="22" type="checkbox" name="documentation" required>  </td> 
      </tr>





      
      <!-- <td>
      <input type="button" onclick="window.location.replace('http://localhost/application/GestionDesStagesV0/index.php?do=lesEntreprises&action=statut')" value="Ajouter un statut d'entreprise" /> </td> -->
      </table>

<!-- Aide pour l'utilisateur signifiant que les champs précédés d'une astérisque sont obligatoires -->
      * : Obligatoire
      <p>
      <input type="submit" name="Valider">  
      <input type="reset" name="Annuler">  
      </div>
</form>