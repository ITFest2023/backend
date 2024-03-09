package smart.bits.spb.SmartParkingBits.ParkingSpotPackage;

import java.text.Collator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import smart.bits.spb.SmartParkingBits.ParkingSpotPackage.ParkingSpotControllers.RegisterRequest;

@Service
public class ParkingSpotServices {
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    private static long FIFTEEN_MILI = 15000;

    public ResponseEntity<String> addNewParkingSpot(ParkingSpotEntity parkingSpotEntity) {

        UUID uuid = parkingSpotEntity.getUuid();

        if(uuid == null)
        {
            return ResponseEntity.notFound().build();
        }

        if(parkingSpotRepository.findById(uuid).isPresent())
        {
            return ResponseEntity.badRequest().body("ESP already exists!");
        }
        
        parkingSpotRepository.saveAndFlush(parkingSpotEntity);
        return ResponseEntity.ok().body("Parking spot created");

    }

    public List<ParkingSpotEntity> getAllParkingSpotsInfo() {

        return parkingSpotRepository
            .findAll()
            .stream()
            .filter(
                
                e -> e.getStatus().equals(ParkingSpotStatus.REGISTERED)).collect(Collectors.toList()
                
            );
    }

    public ParkingSpotEntity getParkingSpotInfoById(UUID uuid) {

        if(uuid == null)
        {
            return null;
        }

        Optional<ParkingSpotEntity> parkingSpot = parkingSpotRepository.findById(uuid);
        if(parkingSpot.isPresent())
        {
            return parkingSpot.get();
        }

        return null;

    }

    public ResponseEntity<String> deleteParkingSpotById(UUID uuid) {

        if(uuid == null)
        {
            return ResponseEntity.notFound().build();
        }

        if(!parkingSpotRepository.findById(uuid).isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        parkingSpotRepository.deleteById(uuid);

        return ResponseEntity.ok().body("Parking spot deleted!");

    }

    public ResponseEntity<String> registerParkingSpotInfo(UUID uuid, RegisterRequest registerRequest) {
        
        if(uuid == null)
        {
            return ResponseEntity.notFound().build();
        }

        ParkingSpotEntity parkingSpotEntity = parkingSpotRepository.findById(uuid).get(); 
        if(parkingSpotEntity != null)
        {
            parkingSpotEntity.setLat(registerRequest.lat());
            parkingSpotEntity.setLng(registerRequest.lng());
            parkingSpotEntity.setStatus(ParkingSpotStatus.REGISTERED);

            parkingSpotRepository.saveAndFlush(parkingSpotEntity);

            return ResponseEntity.ok().body("ESP registered!");
        }
        
        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<String> notifyParkingSpotInfo(ParkingSpotEntity parkingSpotEntity) {

        if(parkingSpotEntity == null)
        {
            return ResponseEntity.notFound().build();
        }

        if(parkingSpotEntity.getStatus().equals(ParkingSpotStatus.UNREGISTERED))
        {
            return ResponseEntity.badRequest().body("ESP not regitered!");
        }

        long currentTime = System.currentTimeMillis();

        if(parkingSpotEntity.getLastTimeReqSec() == 0 && parkingSpotEntity.getBattery() > 20)
        {
            parkingSpotEntity.setLastTimeReqSec(currentTime);
            parkingSpotEntity.setStatus(ParkingSpotStatus.ONLINE);
            parkingSpotRepository.saveAndFlush(parkingSpotEntity);
            return ResponseEntity.ok().build();
        }
        
        if(parkingSpotEntity.getLastTimeReqSec() == 0 && parkingSpotEntity.getBattery() <= 20)
        {
            parkingSpotEntity.setLastTimeReqSec(currentTime);
            parkingSpotEntity.setStatus(ParkingSpotStatus.LOW_BATTERY);
            parkingSpotRepository.saveAndFlush(parkingSpotEntity);
            return ResponseEntity.ok().build();
        }
        
        if(currentTime - parkingSpotEntity.getLastTimeReqSec() <= FIFTEEN_MILI && parkingSpotEntity.getBattery() > 20)
        {
            parkingSpotEntity.setLastTimeReqSec(currentTime);
            parkingSpotEntity.setStatus(ParkingSpotStatus.ONLINE);
            parkingSpotRepository.saveAndFlush(parkingSpotEntity);
            return ResponseEntity.ok().build();
        }
        
        if(currentTime - parkingSpotEntity.getLastTimeReqSec() <= FIFTEEN_MILI && parkingSpotEntity.getBattery() <= 20)
        {
            parkingSpotEntity.setLastTimeReqSec(currentTime);
            parkingSpotEntity.setStatus(ParkingSpotStatus.LOW_BATTERY);
            parkingSpotRepository.saveAndFlush(parkingSpotEntity);
            return ResponseEntity.ok().build();
        }

        parkingSpotEntity.setLastTimeReqSec(0);
        parkingSpotEntity.setStatus(ParkingSpotStatus.OFFLINE);
        parkingSpotRepository.saveAndFlush(parkingSpotEntity);
        return ResponseEntity.ok().build();

    }

}
