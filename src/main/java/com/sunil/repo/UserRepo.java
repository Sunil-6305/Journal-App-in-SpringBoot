package com.sunil.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sunil.entity.JournalEntry;
import com.sunil.entity.User;
public interface UserRepo extends JpaRepository<User, Integer> {
    
    
    
    
    // ✅ User fetch करने के लिए:
    User findByUserName(String userName);

    // ✅ User delete करने के लिए:
    void deleteByUserName(String userName);
    
}



 