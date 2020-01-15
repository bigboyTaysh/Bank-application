package pl.wolski.bank.services;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.UserRepository;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address findExistAddress(Address address){
        pl.wolski.bank.models.Address addressInRepository =
                addressRepository.findByStreetAndHouseNumberAndApartmentNumberAndCityAndZipCode(
                        address.getStreet(), address.getHouseNumber(),
                        address.getApartmentNumber(),
                        address.getCity(), address.getZipCode()
                );
        if(addressInRepository == null) {
            addressRepository.saveAndFlush(address);
            return address;
        }
        return addressInRepository;
    };

    @Override
    public void save(pl.wolski.bank.models.Address address) {
        addressRepository.saveAndFlush(address);
    }
}