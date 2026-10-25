package com.volunteer.dutyroster.config;

import com.volunteer.dutyroster.model.Duty;
import com.volunteer.dutyroster.model.DutyStatus;
import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.repository.DutyRepository;
import com.volunteer.dutyroster.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Seeds the database with default users and sample duties for development.
 * Users: admin/admin123, coordinator/coord123, volunteer1/vol123, volunteer2/vol123
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               DutyRepository dutyRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Only seed if no users exist
            if (userRepository.count() > 0) {
                return;
            }

            // Create default users
            User admin = new User("admin", passwordEncoder.encode("admin123"),
                    "Admin User", "admin@volunteer.org", UserRole.ADMIN);
            admin = userRepository.save(admin);

            User coordinator = new User("coordinator", passwordEncoder.encode("coord123"),
                    "Jane Coordinator", "jane@volunteer.org", UserRole.COORDINATOR);
            coordinator = userRepository.save(coordinator);

            User volunteer1 = new User("volunteer1", passwordEncoder.encode("vol123"),
                    "Alice Johnson", "alice@volunteer.org", UserRole.VOLUNTEER);
            volunteer1 = userRepository.save(volunteer1);

            User volunteer2 = new User("volunteer2", passwordEncoder.encode("vol123"),
                    "Bob Williams", "bob@volunteer.org", UserRole.VOLUNTEER);
            volunteer2 = userRepository.save(volunteer2);

            User volunteer3 = new User("volunteer3", passwordEncoder.encode("vol123"),
                    "Charlie Davis", "charlie@volunteer.org", UserRole.VOLUNTEER);
            volunteer3 = userRepository.save(volunteer3);

            // Create sample duties
            Duty duty1 = new Duty("Morning Park Cleanup", "Clean up litter and debris from the community park",
                    LocalDate.now().plusDays(2), "Central Community Park");
            duty1.setStartTime(LocalTime.of(8, 0));
            duty1.setEndTime(LocalTime.of(11, 0));
            duty1.setVolunteer(volunteer1);
            duty1.setCreatedBy(coordinator);
            duty1.setStatus(DutyStatus.CONFIRMED);
            dutyRepository.save(duty1);

            Duty duty2 = new Duty("Food Bank Sorting", "Sort and organize donated food items",
                    LocalDate.now().plusDays(5), "City Food Bank - Main Warehouse");
            duty2.setStartTime(LocalTime.of(14, 0));
            duty2.setEndTime(LocalTime.of(17, 0));
            duty2.setVolunteer(volunteer2);
            duty2.setCreatedBy(coordinator);
            duty2.setStatus(DutyStatus.ASSIGNED);
            dutyRepository.save(duty2);

            Duty duty3 = new Duty("School Tutoring Session", "Help students with math and science homework",
                    LocalDate.now().plusDays(7), "Lincoln Elementary School");
            duty3.setStartTime(LocalTime.of(15, 30));
            duty3.setEndTime(LocalTime.of(17, 30));
            duty3.setCreatedBy(coordinator);
            duty3.setStatus(DutyStatus.DRAFT);
            dutyRepository.save(duty3);

            Duty duty4 = new Duty("Hospital Visit", "Visit and read to patients in the children's ward",
                    LocalDate.now().plusDays(3), "St. Mary's Hospital");
            duty4.setStartTime(LocalTime.of(10, 0));
            duty4.setEndTime(LocalTime.of(12, 0));
            duty4.setVolunteer(volunteer3);
            duty4.setCreatedBy(coordinator);
            duty4.setStatus(DutyStatus.ASSIGNED);
            dutyRepository.save(duty4);

            Duty duty5 = new Duty("Community Garden Maintenance", "Water plants, pull weeds, and harvest vegetables",
                    LocalDate.now().minusDays(3), "Green Valley Community Garden");
            duty5.setStartTime(LocalTime.of(9, 0));
            duty5.setEndTime(LocalTime.of(12, 0));
            duty5.setVolunteer(volunteer1);
            duty5.setCreatedBy(coordinator);
            duty5.setStatus(DutyStatus.COMPLETED);
            dutyRepository.save(duty5);

            Duty duty6 = new Duty("Fundraiser Event Setup", "Set up tables, chairs and decorations for charity gala",
                    LocalDate.now().plusDays(10), "Grand Ballroom - Downtown Hotel");
            duty6.setStartTime(LocalTime.of(7, 0));
            duty6.setEndTime(LocalTime.of(11, 0));
            duty6.setCreatedBy(coordinator);
            duty6.setStatus(DutyStatus.DRAFT);
            dutyRepository.save(duty6);

            System.out.println("=== Sample data initialized ===");
            System.out.println("Users: admin/admin123, coordinator/coord123, volunteer1/vol123");
        };
    }
}
