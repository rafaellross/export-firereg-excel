package au.com.smartplumbingsolutions.fireregister.fireregisterexcel.entities;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fire_identifications")
@Getter
@Setter
public class Penetration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Integer jobId;

    @Column(name = "fire_number")
    private Integer fireNumber;

    @Column(name = "fire_seal_ref", nullable = false, length = 20)
    private String fireSealRef;

    @Column(name = "fire_resist_level", nullable = false, length = 255)
    private String fireResistLevel;

    @Column(name = "install_dt", nullable = false)
    private Date installDate;

    @Column(name = "install_by", nullable = false, length = 255)
    private String installedBy;

    @Column(nullable = false, length = 255)
    private String manufacturer;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(nullable = false, length = 255)
    private String drawing;

    @Lob
    @Column(name = "fire_photo")
    private String firePhoto;

    @Column(name = "photo_path", length = 255)
    private String photoPath;

    @Column(length = 255)
    private String level;

    @Column(name = "service_description", length = 255)
    private String serviceDescription;

    @Column(nullable = false)
    private Boolean visible;

    @Column(length = 255)
    private String substrate;

}