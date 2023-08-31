package ru.sevmash.timesheetaccounting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.sevmash.timesheetaccounting.domain.TimeSheetEntity;

import java.util.List;

public interface TimeSheetRepository extends JpaRepository<TimeSheetEntity, Long> {
    List<TimeSheetEntity> findAllByPerson_IdAndDeletedIsTrue(Long id, Sort sort);

    List<TimeSheetEntity> findAllByPerson_IdAndDeletedIsFalse(Long id, Sort sort);

    @Query("""
            select t
             from time_sheet t
             where t.person.id = ?1
               and t.deleted = false
            """)
    List<TimeSheetEntity> findByPersonId(@NonNull Long id, Sort sort);

    @Modifying
    @Query("""
           update time_sheet t
           set t.deleted = ?2
           where t.id = ?1
            """)
    int setTimeSheetEntityIsDeleted(Long id, Boolean aBoolean);


    @Modifying
    @Query("""
            update time_sheet as a
            set a.hours = ?2
            where a.id = ?1
            """)
    int changeHoursById(Long id, byte hours);




    List<TimeSheetEntity> findAllByDeletedIsFalse();


}