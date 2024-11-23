package utb.fai.RESTAPIServer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser")
    public ResponseEntity<MyUser> getUserById(@RequestParam(name = "id") Long id) {
        MyUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser newUser) {
        if (!newUser.isUserDataValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            MyUser savedUser = userRepository.save(newUser);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestParam(name = "id") Long id, @RequestBody MyUser updatedUser) {
        MyUser user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            return new ResponseEntity<>("ERROR: User not found.", HttpStatus.NOT_FOUND);
        }

        if (!updatedUser.isUserDataValid()) {
            return new ResponseEntity<>("ERROR: Invalid user data.", HttpStatus.BAD_REQUEST);
        }

        try {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userRepository.save(user);
            return new ResponseEntity<>("INFO: User updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR: Could not update user.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam(name = "id") Long id) {
        MyUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("ERROR: User doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>("INFO: User deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>("INFO: All users deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR occurred while deleting users.", HttpStatus.BAD_REQUEST);
        }
    }
}
