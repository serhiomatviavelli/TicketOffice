package ru.example.ticket_service.repository;

import jooq.db.Tables;
import jooq.db.tables.records.RefreshTokenRecord;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.example.ticket_service.entity.RefreshToken;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class RefreshTokenRepository {

    private final DSLContext context;

    public void saveToken(Long clientId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .client(clientId)
                .token(token)
                .revoked(false)
                .build();

        context.insertInto(Tables.REFRESH_TOKEN)
                .set(context.newRecord(Tables.REFRESH_TOKEN, refreshToken))
                .returning()
                .fetchOptional();
    }

    public Optional<RefreshTokenRecord> findByToken(String token) {
        return context.selectFrom(Tables.REFRESH_TOKEN).where(Tables.REFRESH_TOKEN.TOKEN.eq(token))
                .fetchOptional();
    }

    public void setRevoked(RefreshTokenRecord token) {
        context.update(Tables.REFRESH_TOKEN).set(Tables.REFRESH_TOKEN.REVOKED, true)
                .where(Tables.REFRESH_TOKEN.ID.eq(token.getId()))
                .returning()
                .fetchAny();
    }
}
