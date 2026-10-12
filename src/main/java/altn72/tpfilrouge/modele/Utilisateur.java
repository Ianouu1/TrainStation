package altn72.tpfilrouge.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Utilisateur authentifié de l'application.
 *
 * <p>Le mot de passe n'est jamais stocké en clair : seul son haché (bcrypt) est
 * conservé (cf. sujet §7.7).</p>
 */
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "mot_de_passe_hache", nullable = false, length = 100)
    private String motDePasseHache;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(name = "profil", nullable = false, length = 30)
    private Profil profil;

    protected Utilisateur() {
        // Requis par JPA.
    }

    public Utilisateur(String login, String motDePasseHache, String prenom, Profil profil) {
        this.login = login;
        this.motDePasseHache = motDePasseHache;
        this.prenom = prenom;
        this.profil = profil;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasseHache() {
        return motDePasseHache;
    }

    public void setMotDePasseHache(String motDePasseHache) {
        this.motDePasseHache = motDePasseHache;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    @Override
    public String toString() {
        return "Utilisateur{idUtilisateur=" + idUtilisateur + ", login='" + login + "', profil=" + profil + "}";
    }
}
