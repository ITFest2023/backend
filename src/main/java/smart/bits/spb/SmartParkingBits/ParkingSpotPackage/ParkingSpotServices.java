package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import smart.bits.spb.SmartParkingBits.ParkingSpotPackage.ParkingSpotControllers.RegisterRequest;

@Service
public class ParkingSpotServices {
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;


    public String addNewParkingSpot(ParkingSpotEntity parkingSpotEntity) {

        if(parkingSpotEntity == null)
        {
            return "Could not add new parking spot! Spot null!";
        }

        parkingSpotRepository.saveAndFlush(parkingSpotEntity);
        
        return "New parking spot added!";
        

    }

    public List<ParkingSpotEntity> getAllParkingSpotsInfo() {

        List<ParkingSpotEntity> parkingSpots = parkingSpotRepository.findAll();
        return parkingSpots;

    }

    public ParkingSpotEntity getParkingSpotInfoById(UUID uuid) {

        if(uuid != null)
        {
            Optional<ParkingSpotEntity> parkingSpot = parkingSpotRepository.findById(uuid);
            if(parkingSpot.isPresent())
            {
                return parkingSpot.get();
            }
        }

        return null;

    }

    public ResponseEntity<String> notifyParkingSpotInfo(ParkingSpotEntity parkingSpotEntity) {

        if(parkingSpotEntity == null)
        {
            return ResponseEntity.notFound().build();
        }

        parkingSpotRepository.saveAndFlush(parkingSpotEntity);

        return ResponseEntity.ok().build();

    }

    public String deleteParkingSpotById(UUID uuid) {

        if(uuid == null)
        {
            return "Could not delete parking spot! ID null!";
        }

        parkingSpotRepository.deleteById(uuid);

        return "Parking spot eleted!";

    }

    public String registerParkingSpotInfo(UUID uuid, RegisterRequest registerRequest) {
        
        return null;

    }

}
