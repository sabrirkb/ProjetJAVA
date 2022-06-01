<?php
/** 
 * Classe d'accès aux données. 
  * Utilise les services de la classe PDO
 * pour l'application exempleMVC du cours sur la bdd bddemployés
 * Les attributs sont tous statiques,
 * les 4 premiers pour la connexion
 * $monPdo de type PDO 
 * $monPdo qui contiendra l'unique instance de la classe
 * @package default
 * @author AP modifié par ST
 * @version    1.0
 * @link       http://www.php.net/manual/fr/book.pdo.php
 */

class PdoGsb{   		
      	private static $serveur='mysql:host=gsbvisiteurs.mysql.database.azure.com';
      	private static $bdd='dbname=gsb_visiteurs';   		
      	private static $user='sabrisokhna' ;    		
      	private static $mdp='PPPE-mvc13!' ;
        private static $monPdo;
		private static $monPdoGsb=null;

/**
 * Constructeur privé, crée l'instance de PDO qui sera sollicitée
 * pour toutes les méthodes de la classe
 */				
	private function __construct(){
        try {
    	PdoGsb::$monPdo = new PDO(PdoGsb::$serveur.';'.PdoGsb::$bdd, PdoGsb::$user, PdoGsb::$mdp); 
		PdoGsb::$monPdo->query("SET CHARACTER SET utf8");
	    } catch (Exception $e) {
            throw new Exception("Erreur à la connexion : \n" . $e->getMessage());
        }
    }

	public function _destruct(){
		PdoGsb::$monPdo = null;
	}
/**
 * Fonction statique qui crée l'unique instance de la classe PdoExemple
 
 * Appel : $instancePdoExemple = PdoExemple::getPdoExemple();
 
 * @return l'unique objet de la classe PdoExemple
 */
	public  static function getPdoGsb(){
		if(PdoGsb::$monPdoGsb==null){
			PdoGsb::$monPdoGsb= new PdoGsb();
		}
		return PdoGsb::$monPdoGsb;  
	}
   public function getInfosVisiteur($login,$mdp){
       $req="select VIS_MATRICULE, VIS_NOM ,VIS_PRENOM from visiteur where LOGIN = :login and MDP = :mdp";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$prep->bindValue(':login', $login,PDO::PARAM_STR);
			$prep->bindValue(':mdp', $mdp,PDO::PARAM_STR);
			$prep->execute();
			$result=$prep->fetch(PDO::FETCH_ASSOC);
			return $result;
        }
        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }
   public function getLesVisiteurs() {
      // retourne un tableau associatif contenant tous les visiteurs
        $req="select vis_matricule, VIS_NOM, VIS_VILLE from visiteur";
		$rs = PdoGsb::$monPdo->query($req);
		$ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		return $ligne;
        // ou return $this->_pdo->query($query)->fetchAll(PDO::FETCH_ASSOC);
    }

	// Permet d'obtenir tous les praticiens triés par nom
    public function getPraticiens() {
        $req="select PRA_NUM, PRA_NOM, PRA_PRENOM from praticien order by PRA_NOM";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$rs = PdoGsb::$monPdo->query($req);
		    $ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		    return $ligne;
        }

        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }
	
	// Retourne le nom du praticien dont le numéro est passé en paramètre
	public function getNomPraticien($numeroPraticien) {
		try
		{
			$prep = PdoGsb::$monPdo->prepare("SELECT * FROM praticien WHERE PRA_NUM = :numeroPraticien");
			$prep->bindValue(':numeroPraticien', $numeroPraticien ,PDO::PARAM_STR);
			$prep->execute();
			$result=$prep->fetch(PDO::FETCH_ASSOC);
			return $result['PRA_NOM'];
		}
		catch(Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}
	
	// Retourne le prénom du praticien dont le numéro est passé en paramètre
	public function getPrenomPraticien($numeroPraticien) {
		try
		{
			$prep = PdoGsb::$monPdo->prepare("SELECT * FROM praticien WHERE PRA_NUM = :numeroPraticien");
			$prep->bindValue(':numeroPraticien', $numeroPraticien ,PDO::PARAM_STR);
			$prep->execute();
			$result=$prep->fetch(PDO::FETCH_ASSOC);
			return $result['PRA_PRENOM'];
		}
		catch(Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

   // Permet d'obtenir tous les médicaments
	public function getLesProduits() {
        $req="select MED_DEPOTLEGAL, MED_NOMCOMMERCIAL from medicament order by MED_NOMCOMMERCIAL";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$rs = PdoGsb::$monPdo->query($req);
		    $ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		    return $ligne;
        }

        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }


	// public function AjouterCR($Matricule, $Numero, $praticien, $dateVisite, $bilan, $Motif)
	// {
	// 	$req="insert into rapport_visite(VIS_MATRICULE, RAP_NUM, PRA_NUM, RAP_DATE, RAP_BILAN, RAP_MOTIF) 
	// 	VALUES (:pVIS_MATRICULE, :pRAP_NUM, :pPRA_NUM, :pRAP_DATE, :pRAP_BILAN, :pRAP_MOTIF)";
    //     try {
	// 		$prep = PdoGsb::$monPdo->prepare($req);
	// 		$prep->bindValue(':pVIS_MATRICULE', $Matricule,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_NUM', $Numero,PDO::PARAM_INT);
	// 		$prep->bindValue(':pPRA_NUM', $praticien,PDO::PARAM_INT);
	// 		$prep->bindValue(':pRAP_DATE', $dateVisite,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_BILAN', $bilan,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_MOTIF', $Motif,PDO::PARAM_STR);
	// 		$prep->execute();
	// 		$result=$prep->fetch(PDO::FETCH_ASSOC);
	// 		return $result;
    //     }

    //     catch (Exception $e) 
    //     {
    //         echo 'Exception reçue : ',  $e->getMessage(), "\n";
    //     }
	// }

	public function getLeMatriculeVisiteur()
	{
		
		try
		{
			$prep = PdoGsb::$monPdo->prepare("SELECT * FROM visiteur WHERE VIS_NOM = :vis_nom");
			$prep->bindValue(':vis_nom', $_SESSION['nom'],PDO::PARAM_STR);
			$prep->execute();
			$result=$prep->fetch(PDO::FETCH_ASSOC);
			return $result['VIS_MATRICULE'];
		}
		catch(Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function AjouterCR($Matricule,$Numero,$praticien,$dateVisite,$bilan,$Motif){ 
		$prep = PdoGsb::$monPdo->prepare("INSERT INTO rapport_visite VALUES (:pVIS_MATRICULE, :pRAP_NUM, :pPRA_NUM, :pRAP_DATE, :pRAP_BILAN, :pRAP_MOTIF)");
		try{
			$prep->bindValue(':pVIS_MATRICULE', $Matricule, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_NUM', $Numero, PDO::PARAM_INT);
			$prep->bindValue(':pPRA_NUM', $praticien, PDO::PARAM_INT);
			$prep->bindValue(':pRAP_DATE', $dateVisite, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_BILAN', $bilan, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_MOTIF', $Motif, PDO::PARAM_STR);
			$success = $prep->execute();
		
			if($success){
				return true;
			}else{
				return false;
			}
		}
		catch(Exception $e){
			echo 'Exception reçue : ',  $e->getMessage(), "\n";
		}
	}

	public function getLesMedicaments($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `medicament` LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getLesPraticiens($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `praticien` ORDER BY PRA_NOM LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getLesActivites($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `activite_compl` LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getLesCompteRendus($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `rapport_visite` LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxMedicaments() {
		// retroune le nombre max de médicaments dans la database
		$req = "SELECT COUNT(*) AS MaxMed FROM medicament;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxMed'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxPraticiens() {
		// retroune le nombre max de praticiens dans la database
		$req = "SELECT COUNT(*) AS MaxPrat FROM praticien;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxPrat'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxActivites() {
		// retroune le nombre max de praticiens dans la database
		$req = "SELECT COUNT(*) AS MaxActiv FROM activite_compl;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxActiv'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxCompteRendus() {
		// retroune le nombre max de médicaments dans la database
		$req = "SELECT COUNT(*) AS MaxCR FROM rapport_visite;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxCR'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

    public function retournerLeDernierNumRap()
{
    // renvoie un result avec la liste des raports
    $req = "SELECT RAP_NUM
            FROM rapport_visite
            ORDER BY RAP_NUM DESC
            LIMIT 1";
			try
			{
				$query =PdoGsb::$monPdo -> query($req);
		        $result = $query -> fetch(PDO::FETCH_ASSOC);
		        return $result['RAP_NUM'];
			}
			catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
    
}

public function AjouterAC($Numero, $dateAC, $Lieu, $Theme, $Motif){ 
	$prep = PdoGsb::$monPdo->prepare("INSERT INTO activite_compl VALUES ( :pAC_NUM, :pAC_DATE, :pAC_LIEU, :pAC_THEME, :pAC_MOTIF)");
	try{
		$prep->bindValue(':pAC_NUM', $Numero, PDO::PARAM_INT);
		$prep->bindValue(':pAC_DATE', $dateAC, PDO::PARAM_STR);
		$prep->bindValue(':pAC_LIEU', $Lieu, PDO::PARAM_STR);
		$prep->bindValue(':pAC_THEME', $Theme, PDO::PARAM_STR);
		$prep->bindValue(':pAC_MOTIF', $Motif, PDO::PARAM_STR);
		$success = $prep->execute();
	
		if($success){
			return true;
		}else{
			return false;
		}
	}
	catch(Exception $e){
		echo 'Exception reçue : ',  $e->getMessage(), "\n";
	}
}

public function retournerLeDernierNumAC()
{
    // renvoie un result avec la liste des activités
    $req = "SELECT AC_NUM
            FROM activite_compl
            ORDER BY AC_NUM DESC
            LIMIT 1";
			try
			{
				$query =PdoGsb::$monPdo -> query($req);
		        $result = $query -> fetch(PDO::FETCH_ASSOC);
		        return $result['AC_NUM'];
			}
			catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
    
}

public function getLesLieux() {
	$req="select REG_CODE, REG_NOM from region order by REG_NOM ";
	try {
		$prep = PdoGsb::$monPdo->prepare($req);
		$rs = PdoGsb::$monPdo->query($req);
		$ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		return $ligne;
	}

	catch (Exception $e) 
	{
		echo 'Exception reçue : ',  $e->getMessage(), "\n";
	}
}

public function AjouterPraticien($numero, $nom, $prenom, $adresse, $CP, $ville, $coef, $type){ 
	$prep = PdoGsb::$monPdo->prepare("INSERT INTO praticien VALUES ( :pPRA_NUM, :pPRA_NOM, :pPRA_PRENOM, :pPRA_ADRESSE, :pPRA_CP, :pPRA_VILLE, :pPRA_COEFNOTORIETE, pTYP_CODE)");
	try{
		$prep->bindValue(':pPRA_NUM', $numero, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_NOM', $nom, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_PRENOM', $prenom, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_ADRESSE', $adresse, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_CP', $CP, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_VILLE', $ville, PDO::PARAM_STR);
		$prep->bindValue(':pPRA_COEFNOTORIETE', $coef, PDO::PARAM_STR);
		$prep->bindValue(':pTYP_CODE', $type, PDO::PARAM_STR);
		$success = $prep->execute();
		if($success){
			return true; 
		}else{
			return false;
		}
	}
	catch(Exception $e){
		echo 'Exception reçue : ',  $e->getMessage(), "\n";
	}
}

public function getTypes() {
	$req="select TYP_CODE, TYP_LIBELLE from type_praticien order by TYP_LIBELLE ";
	try {
		$prep = PdoGsb::$monPdo->prepare($req);
		$rs = PdoGsb::$monPdo->query($req);
		$ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		return $ligne;
	}

	catch (Exception $e) 
	{
		echo 'Exception reçue : ',  $e->getMessage(), "\n";
	}
}

public function retournerLeDernierNumPrat()
{
    // renvoie un result avec la liste des praticiens
    $req = "SELECT PRA_NUM
            FROM praticien
            ORDER BY PRA_NUM DESC
            LIMIT 1";
			try
			{
				$query =PdoGsb::$monPdo -> query($req);
		        $result = $query -> fetch(PDO::FETCH_ASSOC);
		        return $result['PRA_NUM'];
			}
			catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
    
}


}


  ?>