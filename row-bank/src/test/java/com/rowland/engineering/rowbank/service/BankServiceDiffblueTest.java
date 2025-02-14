package com.rowland.engineering.rowbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rowland.engineering.rowbank.dto.BeneficiaryRequest;
import com.rowland.engineering.rowbank.dto.BeneficiaryResponse;
import com.rowland.engineering.rowbank.exception.IncorrectBankNameException;
import com.rowland.engineering.rowbank.exception.UserNotFoundException;
import com.rowland.engineering.rowbank.model.BankName;
import com.rowland.engineering.rowbank.model.User;
import com.rowland.engineering.rowbank.repository.SavingHistoryRepository;
import com.rowland.engineering.rowbank.repository.SavingRepository;
import com.rowland.engineering.rowbank.repository.TransactionRepository;
import com.rowland.engineering.rowbank.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BankServiceDiffblueTest {
    /**
     * Method under test:
     * {@link BankService#getBeneficiaryDetails(BeneficiaryRequest)}
     */
    @Test
    void testGetBeneficiaryDetails() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@560d53d3 testClass = com.rowland.engineering.rowbank.service.DiffblueFakeClass2, locations = [], classes = [com.rowland.engineering.rowbank.service.BankService], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@3b1174d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@770df0b4, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@e599af8, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@2526138f], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.Optional.map(Optional.java:260)
        //   See https://diff.blue/R026 to resolve this issue.

        User user = new User();
        user.setAccountNumber("42");
        user.setBalance(new BigDecimal("2.3"));
        user.setBankName(BankName.ROW_BANK);
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setDateOfBirth(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult);
        BankService bankService = new BankService(userRepository, mock(TransactionRepository.class),
                mock(SavingRepository.class), mock(SavingHistoryRepository.class));
        BeneficiaryResponse actualBeneficiaryDetails = bankService
                .getBeneficiaryDetails(new BeneficiaryRequest("42", BankName.ROW_BANK));
        verify(userRepository).findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any());
        assertEquals("42", actualBeneficiaryDetails.getAccountNumber());
        assertEquals("Doe", actualBeneficiaryDetails.getLastName());
        assertEquals("Jane", actualBeneficiaryDetails.getFirstName());
        assertEquals("jane.doe@example.org", actualBeneficiaryDetails.getEmail());
        assertEquals("janedoe", actualBeneficiaryDetails.getUsername());
        assertEquals(1L, actualBeneficiaryDetails.getId().longValue());
        assertEquals(BankName.ROW_BANK, actualBeneficiaryDetails.getBankName());
    }

    /**
     * Method under test:
     * {@link BankService#getBeneficiaryDetails(BeneficiaryRequest)}
     */
    @Test
    void testGetBeneficiaryDetails2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@560d53d3 testClass = com.rowland.engineering.rowbank.service.DiffblueFakeClass2, locations = [], classes = [com.rowland.engineering.rowbank.service.BankService], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@3b1174d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@770df0b4, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@e599af8, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@2526138f], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.Optional.map(Optional.java:260)
        //   See https://diff.blue/R026 to resolve this issue.

        User user = new User();
        user.setAccountNumber("42");
        user.setBalance(new BigDecimal("2.3"));
        user.setBankName(BankName.ACCESS_BANK);
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setDateOfBirth(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult);
        BankService bankService = new BankService(userRepository, mock(TransactionRepository.class),
                mock(SavingRepository.class), mock(SavingHistoryRepository.class));
        assertThrows(IncorrectBankNameException.class,
                () -> bankService.getBeneficiaryDetails(new BeneficiaryRequest("42", BankName.ROW_BANK)));
        verify(userRepository).findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link BankService#getBeneficiaryDetails(BeneficiaryRequest)}
     */
    @Test
    void testGetBeneficiaryDetails3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@560d53d3 testClass = com.rowland.engineering.rowbank.service.DiffblueFakeClass2, locations = [], classes = [com.rowland.engineering.rowbank.service.BankService], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@3b1174d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@770df0b4, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@e599af8, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@2526138f], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.Optional.map(Optional.java:260)
        //   See https://diff.blue/R026 to resolve this issue.

        UserRepository userRepository = mock(UserRepository.class);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(emptyResult);
        BankService bankService = new BankService(userRepository, mock(TransactionRepository.class),
                mock(SavingRepository.class), mock(SavingHistoryRepository.class));
        assertThrows(UserNotFoundException.class,
                () -> bankService.getBeneficiaryDetails(new BeneficiaryRequest("42", BankName.ROW_BANK)));
        verify(userRepository).findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link BankService#getBeneficiaryDetails(BeneficiaryRequest)}
     */
    @Test
    void testGetBeneficiaryDetails4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@560d53d3 testClass = com.rowland.engineering.rowbank.service.DiffblueFakeClass2, locations = [], classes = [com.rowland.engineering.rowbank.service.BankService], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@3b1174d4, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@770df0b4, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@e599af8, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@2526138f], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.Optional.map(Optional.java:260)
        //   See https://diff.blue/R026 to resolve this issue.

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new UserNotFoundException("An error occurred"));
        BankService bankService = new BankService(userRepository, mock(TransactionRepository.class),
                mock(SavingRepository.class), mock(SavingHistoryRepository.class));
        assertThrows(UserNotFoundException.class,
                () -> bankService.getBeneficiaryDetails(new BeneficiaryRequest("42", BankName.ROW_BANK)));
        verify(userRepository).findByAccountNumberOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }
}
