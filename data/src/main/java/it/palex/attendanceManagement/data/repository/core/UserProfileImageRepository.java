package it.palex.attendanceManagement.data.repository.core;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.UserProfileImage;

@Repository
public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long>,
							QuerydslPredicateExecutor<UserProfileImage> {

	/**
	 * The query will find the image that has the <b>compressionNameToCompare</b> and not 
	 * has the <b>compressionNameToFind</b> <br>
	 * @param compressionNameToCompare the compression that exist
	 * @param compressionNameToFind the compression that not exist
	 * @param pageable
	 * @return
	 */
    @Query("SELECT image from UserProfileImage image WHERE "
    		+ "LOWER(image.imageCompression)= lower(?1) AND "
    		+ "not exists("
    				+  "SELECT image2 from UserProfileImage image2 WHERE "
    				+ "image2.userProfile.id=image.userProfile.id AND "
    					+ "LOWER(image2.imageCompression)= lower(?2)"
    				+ ")")
	public List<UserProfileImage> findAllUserProfileImageWithoutCompression
					(String compressionNameToCompare, String compressionNameToFind, 
								Pageable pageable);
    
}
