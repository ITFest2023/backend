package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "parking_spots")
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class ParkingSpotEntity {
    
    @Id
    private UUID uuid;

    private boolean freeSpot;

    private double battery;

    private double lat = 0.0;

    private double lng = 0.0;

    private long lastTimeReqSec = 0;

    @Enumerated(EnumType.STRING)
    private ParkingSpotStatus status = ParkingSpotStatus.UNREGISTERED;


}
