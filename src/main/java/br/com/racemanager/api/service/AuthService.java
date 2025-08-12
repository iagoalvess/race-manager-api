package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.AuthRequest;
import br.com.racemanager.api.dto.AuthResponse;
import br.com.racemanager.api.dto.OrganizerRequest;
import br.com.racemanager.api.dto.OrganizerResponse;
import br.com.racemanager.api.model.Organizer;
import br.com.racemanager.api.repository.OrganizerRepository;
import br.com.racemanager.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OrganizerRepository organizerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var organizer = organizerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
        var jwtToken = jwtService.generateToken(organizer);
        return AuthResponse.builder()
                .token(jwtToken)
                .organizerName(organizer.getName())
                .email(organizer.getEmail())
                .build();
    }

    public OrganizerResponse register(OrganizerRequest request) {
        if (organizerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        var organizer = Organizer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();
        var savedOrganizer = organizerRepository.save(organizer);
        
        return OrganizerResponse.builder()
                .id(savedOrganizer.getId())
                .name(savedOrganizer.getName())
                .email(savedOrganizer.getEmail())
                .build();
    }
}
