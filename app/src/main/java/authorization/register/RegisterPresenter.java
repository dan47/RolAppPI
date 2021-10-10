package authorization.register;

public class RegisterPresenter implements Contract.RegisterListener {

    private authorization.register.Contract.RegisterView registerView;

    private RegisterInteractor registerInteractor;

    public RegisterPresenter(Contract.RegisterView registerView) {
        this.registerView = registerView;
        registerInteractor = new RegisterInteractor( this);
    }

    public void start(RegisterCredentials credentials) {
        registerView.showProgressbar();
        registerInteractor.register(credentials);
    }

    @Override
    public void onSuccess() {
        registerView.hideProgressbar();
        registerView.onSuccess();
    }

    @Override
    public void onFailed(String messsage) {
        registerView.hideProgressbar();
        registerView.onFailed(messsage);
    }

}
