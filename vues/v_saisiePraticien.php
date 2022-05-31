<main id="contenu">
     <h1>Saisir un nouveau praticien</h1>
<form action="index.php?uc=praticiens&action=validerSaisiePraticien" method="post">

<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->
  

      <table style="background-color: transparent">
      <tbody class="consultation">

      <!-- Création d'une zone de texte "numéro praticien" -->

      <tr> <td><p>NUM&Eacute;RO*</p></td>
              <td><input type="number" name="num" value="<?=  $pdo->retournerLeDernierNumPrat()+1; ?>" readonly required></td>
      </tr>

       <!-- Création d'une zone de texte "nom praticien" -->

       <tr> <td><p>NOM*</p></td>
              <td><input type="text" name="nom" required></td>
      </tr>

      <!-- Création d'une zone de texte "prénom praticien" -->

      <tr> <td><p>PR&Eacute;NOM*</p></td>
              <td><input type="text" name="prenom" required></td>
      </tr>

      <!-- Création d'une zone de texte "adresse" -->

      <tr> <td><p>ADRESSE*</p></td>
              <td><input type="text" name="adresse"  placeholder="160 r saitn pierre" required></td>
      </tr>

      <!-- Création d'une zone de texte "code postal" -->

      <tr> <td><p>CODE POSTAL*</p></td>
              <td><input type="text" name="cp" required></td>
      </tr>

      <!-- Création d'une zone de texte "ville" -->

      <tr> <td><p>VILLE*</p></td>
              <td><input type="text" name="ville" onkeyup="this.value=this.value.toUpperCase()" required></td>
      </tr>

      <!-- Création d'une zone de texte "coefficient de notorieté" -->

      <tr> <td><p>COEFFICIENT DE NOTORI&Eacute;T&Eacute;*</p></td>
              <td><input type="text" name="coef" placeholder="250.5" required></td>
      </tr>

      <!-- Création d'une liste déroulante avec une zone de texte possédant le code du type en "value" et le nom en libelle -->
<tr> <td> <label for ="choix_type"> TYPE PRATICIEN*</td></label>
<td ><input list="types" type="text" style="margin: auto; width: 50%" name="choix_typePraticien" required autocomplete = 'off'>
      <datalist id="types">
      <?php 
      $lesTypes = $pdo->getTypes();
      foreach ($lesTypes as $unType) {
                         $codeType = $unType['TYP_CODE'];
                         $libelleType = $unType['TYP_LIBELLE'];
        
      echo '<option value='.$codeType.'&nbsp;-&nbsp;'.$libelleType.'></option>';
}
?>
</datalist> </td> </tr>
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