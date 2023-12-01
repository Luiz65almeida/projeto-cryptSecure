package com.almeida.criptografiasha512.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.almeida.criptografiasha512.common.CryptographyService;
import com.almeida.criptografiasha512.entities.User;
import com.almeida.criptografiasha512.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findById(Long id) {
    Optional<User> obj = repository.findById(id);
    return obj.get();
  }

  public User insert(User user) {

    // Gere um salt aleatório
    String salt = gerarSaltAleatorio();

    // Combine os dados do usuário com o salt
    String documentComSalt = user.getUserDocument() + salt;
    String creditCardTokenComSalt = user.getCreditCardToken() + salt;

    // Criptografe os dados
    String documentCriptografada = CryptographyService.encryptWithSHA512(documentComSalt, salt);
    String creditCardTokenCriptografada = CryptographyService.encryptWithSHA512(creditCardTokenComSalt, salt);

    // Defina os dados criptografados no usuário
    user.setUserDocument(documentCriptografada);
    user.setCreditCardToken(creditCardTokenCriptografada);

    return repository.save(user);

  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public User update(Long id, User obj) {
    User entity = repository.getReferenceById(id);
    updateData(entity, obj);
    return repository.save(entity);
  }

  private void updateData(User entity, User obj) {
    entity.setName(obj.getName());
  }

  private String gerarSaltAleatorio() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] salt = new byte[2]; // Tamanho do salt em bytes

    // Gere um salt aleatório
    secureRandom.nextBytes(salt);

    // Converta o salt em uma representação de string (por exemplo, Base64)
    String saltString = Base64.getEncoder().encodeToString(salt);

    return saltString;
  }
}