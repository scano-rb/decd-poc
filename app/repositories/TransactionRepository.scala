package repositories

import javax.inject.{Inject, Singleton}

@Singleton
class TransactionRepository @Inject()(postgresRepository: PostgresRepository) {}
