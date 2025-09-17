package vn.socialnet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.UserDetailResponse;
import vn.socialnet.model.Role;
import vn.socialnet.model.User;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy) {


        StringBuilder sqlQuery = new StringBuilder("select u from User u where 1=1 ");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and (lower(u.name) like lower(:name)");
            sqlQuery.append(" or lower(u.email) like lower(:email))");
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);

            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc") || matcher.group(3).equalsIgnoreCase("desc")) {
                    sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
                }
            } else {
                throw new InvalidParameterException("Invalid Sort Order Exception");
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("name", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }

        // trong spring boot 2: name phai them : dang truoc, exp: :name

        List<User> users = selectQuery.getResultList();
        List<UserDetailResponse> details = users.stream().map(user -> UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .gender(user.getGender().name())
                .dateOfBirth(user.getDateOfBirth())
                .build()).toList();

        StringBuilder sqlCountQuery = new StringBuilder("select count(u) from User u where 1=1");

        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and (lower(u.name) like lower(?1)");
            sqlCountQuery.append(" or lower(u.email) like lower(?2))");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();

        Page<?> page = new PageImpl<>(details, PageRequest.of(pageNo, pageSize), totalElements);
        return PageResponse.builder()
                .pageNo(pageNo + 1)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }
}
