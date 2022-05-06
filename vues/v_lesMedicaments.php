<form action="" method="post">
    <div id="contenu">
        <h1>Pharmacopee</h1>
        <br>
        <table style="width: 100%">
            <tbody>
                <tr>
                    <td>DEPOT LEGAL</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                        <datalist id="etudiants">
                            <?php  foreach ($lesMedicaments as $unMedicament) { // A TERMINER
			                        $id = $unEtudiant['idetudiant'];
			                        $nom = $unEtudiant['nometu'];
                                    $prenom = $unEtudiant['prenometu'];
                                    echo'<option value='.$id.'>'.$nom.' '.$prenom.'</option>';
                                    }
                            ?>
                        </datalist>
                    </td>
                </tr>
                <tr>
                    <td>NOM COMMERCIAL</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td>FAMILLE</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td>COMPOSITION</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td>EFFETS</td>
                    <td>
                        <input size="30" list="medicaments" type="textarea" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td>CONTRE-INDICATIONS</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td>PRIX ECHANTILLON</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="depot-medicament" autocomplete="off">
                    </td>
                </tr>
            </tbody>
        </table>
        <br>
        <div align="center">
            <input type="submit" id="prec" name="Precedent" value="<">
            <input type="submit" id="suiv" name="Suivant" value=">"> 
        </div>
    </div>
</form>